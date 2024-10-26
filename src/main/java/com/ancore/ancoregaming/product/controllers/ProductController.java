package com.ancore.ancoregaming.product.controllers;

import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.common.ResponseMessage;
import com.ancore.ancoregaming.product.dtos.CreateProductDTO;
import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.IProductService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  private final ModelMapper modelMapper = new ModelMapper();
  @Autowired
  private IProductService productService;

  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<ProductDTO>>> findProducts() {
    List<Product> products = this.productService.findAll();
    List<ProductDTO> productsDTO = modelMapper.map(
            products,
            new TypeToken<List<ProductDTO>>() {
            }.getType());
    ApiResponse<List<ProductDTO>> response = new ApiResponse<>(ResponseMessage.OK, productsDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @Secured("ROLE_ADMIN")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestPart("product") CreateProductDTO product, @ModelAttribute FilesDTO filesDTO) {
    Product newProduct = this.productService.createProduct(product, filesDTO);
    ProductDTO productDTO = modelMapper.map(newProduct, ProductDTO.class);
    ApiResponse<ProductDTO> response = new ApiResponse<>(ResponseMessage.OK, productDTO, null);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ApiResponse<ProductDTO>> findProduct(@PathVariable String productId) {
    Product product = this.productService.findProduct(productId);
    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
    ApiResponse<ProductDTO> response = new ApiResponse<>(ResponseMessage.OK, productDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable String productId) {
    this.productService.destroyProduct(productId);
    ApiResponse response = new ApiResponse<>(ResponseMessage.OK, null, null);
    return ResponseEntity.status(200).body(response);
  }
}
