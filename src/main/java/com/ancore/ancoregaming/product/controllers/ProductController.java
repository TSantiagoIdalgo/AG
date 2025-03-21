package com.ancore.ancoregaming.product.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.product.dtos.CreateProductDTO;
import com.ancore.ancoregaming.product.dtos.FilesDTO;
import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.product.dtos.ProductFilterDTO;
import com.ancore.ancoregaming.product.dtos.UpdateProductDTO;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.IProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

  private final ModelMapper modelMapper = new ModelMapper();
  private final IProductService productService;
  
  @Autowired
  public ProductController(IProductService productService) {
    this.productService = productService;
  }
  
  @GetMapping("/")
  public ApiEntityResponse<Page<ProductDTO>> findProducts(@ModelAttribute ProductFilterDTO filterDTO) {
    Page<Product> products = this.productService.findAll(filterDTO);
    Page<ProductDTO> productsDTO = products.map((product) -> modelMapper.map(product, ProductDTO.class));

    ApiResponse<Page<ProductDTO>> response = new ApiResponse<>(productsDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @Secured("ROLE_ADMIN")
  @PostMapping("/create")
  public ApiEntityResponse<ProductDTO> createProduct(@Valid @RequestPart("product") CreateProductDTO product,
      @Valid @ModelAttribute FilesDTO filesDTO) {
    Product newProduct = this.productService.createProduct(product, filesDTO);
    ProductDTO productDTO = modelMapper.map(newProduct, ProductDTO.class);
    ApiResponse<ProductDTO> response = new ApiResponse<>(productDTO, null);
    return ApiEntityResponse.of(HttpStatus.CREATED, response);
  }

  @GetMapping("/{productId}")
  public ApiEntityResponse<ProductDTO> findProduct(@PathVariable String productId) {
    Product product = this.productService.findProduct(productId);
    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
    ApiResponse<ProductDTO> response = new ApiResponse<>(productDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{productId}")
  public ApiEntityResponse<?> deleteProduct(@PathVariable String productId) {
    this.productService.destroyProduct(productId);
    ApiResponse<?> response = new ApiResponse<>(null, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @Secured("ROLE_ADMIN")
  @PatchMapping("/update/{productId}")
  public ApiEntityResponse<ProductDTO> patchProductFields(
      @PathVariable String productId,
      @RequestPart(value = "product", required = false) UpdateProductDTO updateProductDTO,
      @RequestPart(value = "images", required = false) List<MultipartFile> images,
      @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage,
      @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
      @RequestPart(value = "trailer", required = false) MultipartFile trailer) {
    FilesDTO filesDTO = new FilesDTO(mainImage, trailer, backgroundImage, images);
    Product product = this.productService.updateProductFields(productId, updateProductDTO, filesDTO);
    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
    ApiResponse<ProductDTO> response = new ApiResponse<>(productDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
}
