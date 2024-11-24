package com.ancore.ancoregaming.whitelist.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.whitelist.dtos.WhiteListDTO;
import com.ancore.ancoregaming.whitelist.model.Whitelist;
import com.ancore.ancoregaming.whitelist.services.IWhiteListService;

@RestController
@RequestMapping("/whitelist")
public class WhitelistController {

  @Autowired
  private IWhiteListService whitelistService;
  private final ModelMapper modelMapper = new ModelMapper();

  @Secured("ROLE_ADMIN")
  @GetMapping("/")
  public ApiEntityResponse<List<WhiteListDTO>> findAllWhitelists() {
    List<Whitelist> whitelist = this.whitelistService.findAllWhitelist();
    List<WhiteListDTO> whitelistsDTO = modelMapper.map(whitelist,
        new TypeToken<List<WhiteListDTO>>() {
        }.getType());
    ApiResponse<List<WhiteListDTO>> response = new ApiResponse<>(whitelistsDTO, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/product/{productId}")
  public ApiEntityResponse<WhiteListDTO> findWhiteListByProduct(@AuthenticationPrincipal UserDetails user,
      @PathVariable String productId) {
    Whitelist userWhiteList = this.whitelistService.findByUserAndProduct(user.getUsername(), productId);
    WhiteListDTO userWhitelistDTO = modelMapper.map(userWhiteList, WhiteListDTO.class);
    ApiResponse<WhiteListDTO> response = new ApiResponse<>(userWhitelistDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/user")
  public ApiEntityResponse<WhiteListDTO> findWhiteListByUser(@AuthenticationPrincipal UserDetails user) {
    Whitelist userWhiteList = this.whitelistService.findUserWhiteList(user.getUsername());
    WhiteListDTO userWhitelistDTO = modelMapper.map(userWhiteList, WhiteListDTO.class);
    ApiResponse<WhiteListDTO> response = new ApiResponse<>(userWhitelistDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @PostMapping("/{productId}")
  public ApiEntityResponse<WhiteListDTO> addProduct(@AuthenticationPrincipal UserDetails user,
      @PathVariable String productId) throws Exception {
    Whitelist userWhiteList = this.whitelistService.addProductToWhitelist(user.getUsername(), productId);
    WhiteListDTO userWhitelistDTO = modelMapper.map(userWhiteList, WhiteListDTO.class);
    ApiResponse<WhiteListDTO> response = new ApiResponse<>(userWhitelistDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @DeleteMapping("/{productId}")
  public ApiEntityResponse<WhiteListDTO> removeProduct(@AuthenticationPrincipal UserDetails user,
      @PathVariable String productId) {
    Whitelist userWhiteList = this.whitelistService.removeProductFromWhitelist(user.getUsername(), productId);
    WhiteListDTO userWhitelistDTO = modelMapper.map(userWhiteList, WhiteListDTO.class);
    ApiResponse<WhiteListDTO> response = new ApiResponse<>(userWhitelistDTO, null);

    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
}
