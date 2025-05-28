package com.ancore.ancoregaming.product.services.product;

import com.ancore.ancoregaming.product.dtos.CreateProductDTO;
import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductFilterDTO;
import com.ancore.ancoregaming.product.dtos.UpdateProductDTO;
import com.ancore.ancoregaming.product.model.*;
import com.ancore.ancoregaming.product.repositories.IProductRepository;
import com.ancore.ancoregaming.product.services.genre.GenreService;
import com.ancore.ancoregaming.product.services.platform.PlatformService;
import com.ancore.ancoregaming.product.services.requirements.RequirementsService;
import com.ancore.ancoregaming.product.services.upload.UploadService;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService implements IProductService {

  private final GenreService genreService;
  private final PlatformService platformService;
  private final IProductRepository productRepository;
  private final UploadService uploadService;
  private final RequirementsService requirementService;

  @Autowired
  public ProductService(GenreService genreService, PlatformService platformService, IProductRepository productRepository, UploadService uploadService, RequirementsService requirementService) {
    this.genreService = genreService;
    this.platformService = platformService;
    this.productRepository = productRepository;
    this.uploadService = uploadService;
    this.requirementService = requirementService;
  }

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
        .setRelease_date(product.release_date)
        .setDistributor(product.distributor)
        .setPegi(product.pegi)
        .build();
    return this.uploadProductFiles(newProduct, filesDTO, Optional.empty());
  }

  @Override
  public Page<Product> findAll(ProductFilterDTO filterDTO) {
    Specification<Product> spec = ProductSpecificationService
        .orderByCheckoutCount(filterDTO.isOrderByCheckoutCount())
        .and(ProductSpecificationService.orderByWishListCount(filterDTO.isOrderByWishList()))
        .and(ProductSpecificationService.orderByRecommendationCount(filterDTO.isOrderByRecommendation()))
        .and(ProductSpecificationService.orderByCreatedAt(filterDTO.isOrderByCreatedAt()))
        .and(ProductSpecificationService.orderByPrice(filterDTO.isOrderByPrice()))
        .and(ProductSpecificationService.orderByDiscount(filterDTO.isOrderByDiscount()))
        .and(ProductSpecificationService.hasDeveloper(filterDTO.getDeveloper()))
        .and(ProductSpecificationService.hasDistributor(filterDTO.getDistributor()))
        .and(ProductSpecificationService.hasPriceRange(filterDTO.getMinPrice(), filterDTO.getMaxPrice()))
        .and(ProductSpecificationService.hasDiscountRange(filterDTO.getMinDiscount(), filterDTO.getMaxDiscount()))
        .and(ProductSpecificationService.hasFranchise(filterDTO.getFranchise()))
        .and(ProductSpecificationService.hasGenres(filterDTO.getGenres()))
        .and(ProductSpecificationService.hasTags(filterDTO.getTags()))
        .and(ProductSpecificationService.hasKeyword(filterDTO.getName()))
        .and(ProductSpecificationService.hasPlatform(filterDTO.getPlatform()))
        .and(ProductSpecificationService.hasSystem(filterDTO.getSystem()));

    Pageable pageable = PageRequest.of(filterDTO.getPageNumber(), filterDTO.getPageSize());
    return productRepository.findAll(spec, pageable);
  }

  @Override
  public Product findProduct(String productId) {
    return this.productRepository.findById(UUID.fromString(productId))
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }
  
  @Override
  public ProductWithUserWishlistAndPurchased findProductWithWishlistAndPurchase(UserDetails userDetails, UUID productId) {
    if (userDetails != null) return this.productRepository.findProductWithUserWishlistAndPurchase(userDetails.getUsername(), productId);
    return this.productRepository.findProductWithUserWishlistAndPurchase(null, productId);
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
    updateProduct.getName().ifPresent(product::setName);
    updateProduct.getDescription().ifPresent(product::setDescription);
    updateProduct.getDeveloper().ifPresent(product::setDeveloper);
    updateProduct.getDistributor().ifPresent(product::setDistributor);
    updateProduct.getDisabled().ifPresent(product::setDisabled);
    updateProduct.getStock().ifPresent(product::setStock);
    updateProduct.getPegi().ifPresent(product::setPegi);
    updateProduct.getPrice().ifPresent(product::setPrice);
    updateProduct.getDiscount().ifPresent(product::setDiscount);
    updateProduct.getRelease_date().ifPresent(product::setRelease_date);
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
    updateProduct.getRequirements().ifPresent(requirements -> {
      requirements.forEach(requirement -> {
        Optional<Requirements> optionalRequirements = product
            .getRequirements()
            .stream()
            .filter(pr -> pr.getId().equals(requirement.getId()))
            .findFirst();
        
        if (optionalRequirements.isPresent()) {
          Requirements productRequirement = optionalRequirements.get();
          productRequirement.setOs(requirement.getOs());
          productRequirement.setMemory(requirement.getMemory());
          productRequirement.setGraphics(requirement.getGraphics());
          productRequirement.setDirectx_v(requirement.getDirectx_v());
          productRequirement.setStorage(requirement.getStorage());
          productRequirement.setProcessor(requirement.getProcessor());
        }
      });
    });
    return this.uploadProductFiles(product, filesDTO, updateProduct.getImages());
  }

  private Product uploadProductFiles(Product product, FilesDTO filesDTO, Optional<List<String>> images) {
    try {
      product.setMainImage(updateMediaField(product.getMainImage(), filesDTO.getMainImage(), true));
      product.setBackgroundImage(updateMediaField(product.getBackgroundImage(), filesDTO.getBackgroundImage(), true));
      product.setTrailer(updateMediaField(product.getTrailer(), filesDTO.getTrailer(), false));
      if (images.isPresent()) {
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
