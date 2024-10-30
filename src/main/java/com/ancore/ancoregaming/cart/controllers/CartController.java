package com.ancore.ancoregaming.cart.controllers;

import com.ancore.ancoregaming.cart.dtos.UserCartDTO;
import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.services.ICartService;
import com.ancore.ancoregaming.common.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

  // Mapear todos los controllers a UserCartDTO
  @Autowired
  private ICartService cartService;
  private final ModelMapper modelMapper = new ModelMapper();

  @GetMapping("/")
  public ResponseEntity<ApiResponse<UserCartDTO>> getUserCart(@AuthenticationPrincipal UserDetails user) {
    Cart cart = this.cartService.getUserCart(user);
    UserCartDTO userCart = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(HttpStatus.OK, userCart, null);
    return ResponseEntity.status(200).body(response);
  }

  @GetMapping("/paid")
  public ResponseEntity<ApiResponse<UserCartDTO>> getUserPaidProducts(@AuthenticationPrincipal UserDetails user) {
    Cart cart = this.cartService.getUserPaidProducts(user);
    UserCartDTO userCartDTO = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(HttpStatus.OK, userCartDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @PostMapping("/increase/{productId}")
  public ResponseEntity<ApiResponse<UserCartDTO>> increaseProductCart(@PathVariable String productId, @AuthenticationPrincipal UserDetails user) {
    Cart cart = this.cartService.increaseProducts(user, productId);
    UserCartDTO userCartDTO = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(HttpStatus.OK, userCartDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @PostMapping("/decrease/{productId}")
  public ResponseEntity<ApiResponse<UserCartDTO>> decreaseProductCart(@PathVariable String productId, @AuthenticationPrincipal UserDetails user) {
    Cart cart = this.cartService.decreaseProduct(user, productId);
    UserCartDTO userCartDTO = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(HttpStatus.OK, userCartDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @DeleteMapping("/delete/{productId}")
  public ResponseEntity<ApiResponse<UserCartDTO>> deleteProductCart(@PathVariable String productId, @AuthenticationPrincipal UserDetails user) {
    Cart cart = this.cartService.removeProduct(user, productId);
    UserCartDTO userCartDTO = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(HttpStatus.OK, userCartDTO, null);
    return ResponseEntity.status(200).body(response);
  }
}
