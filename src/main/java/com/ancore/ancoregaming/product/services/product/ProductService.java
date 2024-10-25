package com.ancore.ancoregaming.product.services.product;

import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.repositories.IProductRepository;
import com.ancore.ancoregaming.product.services.genre.GenreService;
import com.ancore.ancoregaming.product.services.platform.PlatformService;
import com.ancore.ancoregaming.product.services.upload.UploadService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Override
  public Product createProduct(ProductDTO product, FilesDTO filesDTO) {
    Product newProduct = new Product.Builder()
            .setName(product.name)
            .setDescription(product.description)
            .setPlatforms(this.platformService.bulkCreatePlatforms(product.platforms))
            .setDeveloper(product.developer)
            .setGenres(this.genreService.bulkCreateGenres(product.genres))
            .setStock(product.stock)
            .setPrice(product.price)
            .setDisabled(product.disabled || false)
            .setDiscount(product.discount)
            .build();
    this.productRepository.save(newProduct);
    Product productWithFiles = this.uploadProductFiles(newProduct, filesDTO);
    return productWithFiles;
  }

  @Override
  public List<Product> findAll() {
    return this.productRepository.findAll();
  }

  @Override
  public Product findProduct(UUID productId) {
    return this.productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }

  private Product uploadProductFiles(Product product, FilesDTO filesDTO) {
    try {
      String mainImageUrl = this.uploadService.uploadImage(filesDTO.getMainImage());
      product.setMainImage(mainImageUrl);

      String bgImageUrl = this.uploadService.uploadImage(filesDTO.getBackgroundImage());
      product.setBackgroundImage(bgImageUrl);

      String trailerUrl = this.uploadService.uploadVideo(filesDTO.getTrailer());
      product.setTrailer(trailerUrl);

      List<String> imagesUrls = this.uploadService.bulkUploadFiles(filesDTO.getImages());
      product.setImages(imagesUrls);

      this.productRepository.save(product);
      return product;
    } catch (Exception ex) {
      this.productRepository.save(product);
      return product;
    }
  }
}
