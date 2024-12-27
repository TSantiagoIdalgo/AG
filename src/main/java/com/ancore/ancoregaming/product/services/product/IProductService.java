package com.ancore.ancoregaming.product.services.product;

import com.ancore.ancoregaming.product.dtos.CreateProductDTO;
import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductFilterDTO;
import com.ancore.ancoregaming.product.dtos.UpdateProductDTO;
import com.ancore.ancoregaming.product.model.Product;
import org.springframework.data.domain.Page;

public interface IProductService {

  public Product createProduct(CreateProductDTO product, FilesDTO filesDTO);

  public Page<Product> findAll(ProductFilterDTO filterDTO);

  public Product findProduct(String productId);

  public void destroyProduct(String productId);

  public Product updateProductFields(String productId, UpdateProductDTO updateProduct, FilesDTO filesDTO);
}