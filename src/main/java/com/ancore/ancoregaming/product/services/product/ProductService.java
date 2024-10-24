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
import java.util.logging.Level;
import java.util.logging.Logger;
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
  public Product createProduct(ProductDTO product) {
    System.out.println("DTO: " + product.toString());

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

    return newProduct;
  }

  @Override
  public List<Product> findAll() {
    return this.productRepository.findAll();
  }

  public Product uploadProductFiles(UUID productId, FilesDTO filesDTO) {
    Product product = this.findProduct(productId);
    try {
      String mainImageUrl = this.uploadService.uploadFile(filesDTO.getMainImage());
      product.setMainImage(mainImageUrl);

      String bgImageUrl = this.uploadService.uploadFile(filesDTO.getBackgroundImage());
      product.setBackgroundImage(bgImageUrl);

      String trailerUrl = this.uploadService.uploadFile(filesDTO.getTrailer());
      product.setTrailer(trailerUrl);

      List<String> imagesUrls = this.uploadService.bulkUploadFiles(filesDTO.getImages());
      product.setImages(imagesUrls);

      this.productRepository.save(product);
      return product;
    } catch (Exception ex) {
      Logger.getLogger(ProductService.class.getName()).log(Level.SEVERE, null, ex);
      this.productRepository.save(product);
      return product;
    }
  }

  @Override
  public Product findProduct(UUID productId) {
    return this.productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
  }
}
