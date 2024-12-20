package com.ancore.ancoregaming.product.services.product;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ancore.ancoregaming.product.dtos.CreateProductDTO;
import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductFilterDTO;
import com.ancore.ancoregaming.product.dtos.UpdateProductDTO;
import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.repositories.IProductRepository;
import com.ancore.ancoregaming.product.services.genre.GenreService;
import com.ancore.ancoregaming.product.services.platform.PlatformService;
import com.ancore.ancoregaming.product.services.requirements.RequirementsService;
import com.ancore.ancoregaming.product.services.upload.UploadService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService implements IProductService {

  @Autowired
  private GenreService genreService;
  @Autowired
  private PlatformService platformService;
  @Autowired
  private IProductRepository productRepository;
  @Autowired
  private UploadService uploadService;
  @Autowired
  private RequirementsService requirementService;

  @Override
  public Product createProduct(CreateProductDTO product, FilesDTO filesDTO) {
    Product newProduct = new Product.Builder()
        .setName(product.name)
        .setDescription(product.description)
        .setPlatforms(this.platformService.bulkCreatePlatforms(product.platforms))
        .setDeveloper(product.developer)
        .setFranchise(product.franchise)
        .setRequirements(this.requirementService.bulkCreateRequirements(product.requirements))
        .setGenres(this.genreService.bulkCreateGenres(product.genres))
        .setTags(product.tags)
        .setStock(product.stock)
        .setPrice(product.price)
        .setDisabled(product.disabled)
        .setDiscount(product.discount)
        .build();
    Product productWithFiles = this.uploadProductFiles(newProduct, filesDTO, null);
    return productWithFiles;
  }

  @Override
  public Page<Product> findAll(ProductFilterDTO filterDTO) {
    Specification<Product> spec = ProductSpecificationService
        .orderByCheckoutCount(filterDTO.isOrderByCheckoutCount())
        .and(ProductSpecificationService.orderByWishListCount(filterDTO.isOrderbyWishList()))
        .and(ProductSpecificationService.orderByRecommendationCount(filterDTO.isOrderByRecommendation()))
        .and(ProductSpecificationService.hasDeveloper(filterDTO.getDeveloper()))
        .and(ProductSpecificationService.hasPriceRange(filterDTO.getMinPrice(), filterDTO.getMaxPrice()))
        .and(ProductSpecificationService.hasDiscountRange(filterDTO.getMinDiscount(), filterDTO.getMaxDiscount()))
        .and(ProductSpecificationService.hasFranchise(filterDTO.getFranchise()))
        .and(ProductSpecificationService.hasGenres(filterDTO.getGenres()))
        .and(ProductSpecificationService.hasName(filterDTO.getName()))
        .and(ProductSpecificationService.hasPlatform(filterDTO.getPlatform()))
        .and(ProductSpecificationService.hasTags(filterDTO.getTags()));

    Pageable pageable = PageRequest.of(filterDTO.getPageNumber(), filterDTO.getPageSize());
    Page<Product> pagedResult = productRepository.findAll(spec, pageable);
    return pagedResult;
  }

  @Override
  public Product findProduct(String productId) {
    return this.productRepository.findById(UUID.fromString(productId))
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }

  @Override
  public void destroyProduct(String productId) {
    Product product = this.findProduct(productId);
    try {
      this.uploadService.deleteImage(product.getMainImage());
      product.setMainImage(null);

      this.uploadService.deleteImage(product.getBackgroundImage());
      product.setBackgroundImage(null);

      this.uploadService.deleteVideo(product.getTrailer());
      product.setTrailer(null);

      this.uploadService.bulkDeleteFiles(product.getImages());
      product.getImages().clear();

      this.productRepository.delete(product);
    } catch (Exception ex) {
      this.productRepository.save(product);
      throw new DataIntegrityViolationException("Error deleting entity: " + ex.getMessage());
    }
  }

  @Override
  public Product updateProductFields(String productId, UpdateProductDTO updateProduct, FilesDTO filesDTO) {
    Product product = this.findProduct(productId);
    for (Method method : updateProduct.getClass().getMethods()) {
      if (method.getName().startsWith("get") && method.getReturnType().equals(Optional.class)) {
        try {
          Optional<?> value = (Optional<?>) method.invoke(updateProduct);
          if (value != null) {
            value.ifPresent(val -> setProductField(product, method.getName().substring(3), val));
          }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          throw new RuntimeException("Error actualizando campos del producto: " + e.getMessage());
        }
      }
    }

    // Actualización explícita de las listas si están presentes
    updateProduct.getTags().ifPresent(product::setTags);
    updateProduct.getPlatforms().ifPresent(platforms -> {
      List<Platform> platform = this.platformService.bulkCreatePlatforms(platforms);
      product.setPlatforms(platform);
    });
    updateProduct.getGenres().ifPresent(genres -> {
      List<Genre> genre = this.genreService.bulkCreateGenres(genres);
      product.setGenres(genre);
    });
    Product productWithFilesUpdated = this.uploadProductFiles(product, filesDTO, updateProduct.getImages());
    return productWithFilesUpdated;
  }

  private void setProductField(Product product, String fieldName, Object value) {
    try {
      if (value instanceof List || value == null) {
        return;
      }
      Method setter = product.getClass().getMethod("set" + fieldName, value.getClass());
      setter.invoke(product, value);
    } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException
        | InvocationTargetException e) {
      throw new RuntimeException("Error asignando el campo " + fieldName + "  " + e.getMessage());
    }
  }

  private Product uploadProductFiles(Product product, FilesDTO filesDTO, Optional<List<String>> images) {
    try {
      product.setMainImage(updateMediaField(product.getMainImage(), filesDTO.getMainImage(), true));
      product.setBackgroundImage(updateMediaField(product.getBackgroundImage(), filesDTO.getBackgroundImage(), true));
      product.setTrailer(updateMediaField(product.getTrailer(), filesDTO.getTrailer(), false));
      if (images != null) {
        updateProductImages(product, filesDTO, images.get());
      } else if (filesDTO.hasNonNullFields()) {
        updateProductImages(product, filesDTO, null);
      }

      return this.productRepository.save(product);
    } catch (Exception ex) {
      return this.productRepository.save(product);
    }
  }

  private void updateProductImages(Product product, FilesDTO filesDTO, List<String> images) throws Exception {
    List<String> currentImages = product.getImages();
    if (filesDTO.getImages() == null) {
      return; // No images to upload
    }
    if (images == null) {
      product.setImages(this.uploadService.bulkUploadFiles(filesDTO.getImages()));
      return;
    }

    // Find missing images to delete and update product images
    List<String> missingImages = new ArrayList<>(currentImages);
    missingImages.removeAll(images);
    this.uploadService.bulkDeleteFiles(missingImages);

    List<String> updatedImages = new ArrayList<>(images);
    List<String> newImagesUrls = this.uploadService.bulkUploadFiles(filesDTO.getImages());
    updatedImages.addAll(newImagesUrls);

    product.setImages(updatedImages);
  }

  private String updateMediaField(String currentMediaUrl, MultipartFile newMediaFile, boolean isImage)
      throws Exception {
    if (newMediaFile != null) {
      // Si hay una imagen actual, la elimina
      if (currentMediaUrl != null) {
        if (isImage) {
          this.uploadService.deleteImage(currentMediaUrl);
        } else {
          this.uploadService.deleteVideo(currentMediaUrl);
        }
      }
      // Sube el nuevo archivo según el tipo (imagen o video)
      return isImage ? uploadService.uploadImage(newMediaFile) : uploadService.uploadVideo(newMediaFile);
    }
    // Si no hay nuevo archivo, conserva el existente
    return currentMediaUrl;
  }
}
