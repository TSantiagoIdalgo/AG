package com.ancore.ancoregaming.product.services.product;

import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.product.model.Product;
import java.util.List;
import java.util.UUID;

public interface IProductService {

  public Product createProduct(ProductDTO product);

  public List<Product> findAll();

  public Product uploadProductFiles(UUID productId, FilesDTO filesDTO);

  public Product findProduct(UUID productId);
}
