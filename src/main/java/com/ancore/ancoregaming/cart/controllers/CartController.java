package com.ancore.ancoregaming.cart.controllers;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ancore.ancoregaming.cart.components.AddProductCommand;
import com.ancore.ancoregaming.cart.components.CommandFactory;
import com.ancore.ancoregaming.cart.components.DecreaseProductCommand;
import com.ancore.ancoregaming.cart.components.RemoveProductCommand;
import com.ancore.ancoregaming.cart.dtos.UserCartDTO;
import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.services.ICartService;
import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;

@RestController
@RequestMapping("/cart")
public class CartController {

  private final ICartService cartService;
  private final CommandFactory commandFactory;
  private final ModelMapper modelMapper = new ModelMapper();

  @Autowired
  public CartController(CommandFactory commandFactory, ICartService cartService) {
    this.commandFactory = commandFactory;
    this.cartService = cartService;
  }

  @GetMapping("/")
  public ApiEntityResponse<UserCartDTO> getUserCart(@AuthenticationPrincipal UserDetails user) {
    Cart cart = this.cartService.getUserCart(user);
    UserCartDTO userCart = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(userCart, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/paid")
  public ApiEntityResponse<UserCartDTO> getUserPaidProducts(@AuthenticationPrincipal UserDetails user) {
    Cart cart = this.cartService.getUserPaidProducts(user);
    UserCartDTO userCartDTO = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(userCartDTO, null);
    return ApiEntityResponse.of(HttpStatus.ACCEPTED, response);
  }

  @PostMapping("/increase/{productId}")
  public ApiEntityResponse<UserCartDTO> increaseProductCart(@PathVariable String productId,
      @AuthenticationPrincipal UserDetails user) throws BadRequestException {
    AddProductCommand command = commandFactory.createAddProductCommand(productId, user);
    Cart cart = command.execute();
    UserCartDTO userCartDTO = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(userCartDTO, null);
    return ApiEntityResponse.of(HttpStatus.CREATED, response);
  }

  @PostMapping("/decrease/{productId}")
  public ApiEntityResponse<UserCartDTO> decreaseProductCart(@PathVariable String productId,
      @AuthenticationPrincipal UserDetails user) {
    DecreaseProductCommand command = commandFactory.createDecreaseProductCommand(productId, user);
    Cart cart = command.execute();
    UserCartDTO userCartDTO = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(userCartDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @DeleteMapping("/remove/{productId}")
  public ApiEntityResponse<UserCartDTO> deleteProductCart(@PathVariable String productId,
      @AuthenticationPrincipal UserDetails user) {
    RemoveProductCommand command = commandFactory.createRemoveProductCommand(productId, user);
    Cart cart = command.execute();
    UserCartDTO userCartDTO = modelMapper.map(cart, UserCartDTO.class);
    ApiResponse<UserCartDTO> response = new ApiResponse<>(userCartDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

}
