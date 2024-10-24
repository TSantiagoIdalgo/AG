package com.ancore.ancoregaming.product.controllers;

import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.IProductService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private IProductService productService;

  @GetMapping("/")
  public List<Product> findAllProducts() {
    return this.productService.findAll();
  }

  @Secured("ROLE_ADMIN")
  @PostMapping("/create")
  public Product createProduct(@RequestBody ProductDTO product) {
    return this.productService.createProduct(product);
  }

  @PostMapping("/files/{productId}")
  public Product uploadProductFiles(@PathVariable String productId, @ModelAttribute FilesDTO filesDTO) {
    return this.productService.uploadProductFiles(UUID.fromString(productId), filesDTO);
  }
}
