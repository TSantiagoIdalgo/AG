package com.ancore.ancoregaming.product.services.product;

import com.ancore.ancoregaming.product.dtos.CreateProductDTO;
import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.model.Product;
import java.util.List;

public interface IProductService {

  public Product createProduct(CreateProductDTO product, FilesDTO filesDTO);

  public List<Product> findAll();

  public Product findProduct(String productId);

  public void destroyProduct(String productId);

  public Product updateProduct(String productId);
}
