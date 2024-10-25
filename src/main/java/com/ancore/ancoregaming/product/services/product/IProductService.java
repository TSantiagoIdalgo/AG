package com.ancore.ancoregaming.product.services.product;

import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.product.model.Product;
import java.util.List;
import java.util.UUID;

public interface IProductService {

  public Product createProduct(ProductDTO product, FilesDTO filesDTO);

  public List<Product> findAll();

  public Product findProduct(UUID productId);
}
