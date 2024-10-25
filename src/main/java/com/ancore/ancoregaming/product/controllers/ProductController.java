package com.ancore.ancoregaming.product.controllers;

import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.common.ResponseMessage;
import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.IProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
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
  public ResponseEntity<ApiResponse<Product>> createProduct(@RequestPart("product") ProductDTO product, @ModelAttribute FilesDTO filesDTO) {
    Product newProduct = this.productService.createProduct(product, filesDTO);
    ApiResponse<Product> response = new ApiResponse<>(ResponseMessage.OK, newProduct, null);
    return ResponseEntity.status(201).body(response);
  }
}
