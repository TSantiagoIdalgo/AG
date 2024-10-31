package com.ancore.ancoregaming.whitelist.controllers;

import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.whitelist.dtos.WhiteListDTO;
import com.ancore.ancoregaming.whitelist.model.Whitelist;
import com.ancore.ancoregaming.whitelist.services.IWhiteListService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
@RequestMapping("/whitelist")
public class WhitelistController {

  @Autowired
  private IWhiteListService whitelistService;
  private final ModelMapper modelMapper = new ModelMapper();

  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<WhiteListDTO>>> findAllWhitelists() {
    List<Whitelist> whitelist = this.whitelistService.findAllWhitelist();
    List<WhiteListDTO> whitelistsDTO = modelMapper.map(whitelist,
            new TypeToken<List<WhiteListDTO>>() {
            }.getType());
    ApiResponse<List<WhiteListDTO>> response = new ApiResponse<>(HttpStatus.OK, whitelistsDTO, null);
    return ResponseEntity.status(200).body(response);
  }

  @GetMapping("/user")
  public ResponseEntity<ApiResponse<WhiteListDTO>> findWhiteListByUser(@AuthenticationPrincipal UserDetails user) {
    Whitelist userWhiteList = this.whitelistService.findUserWhiteList(user.getUsername());
    WhiteListDTO userWhitelistDTO = modelMapper.map(userWhiteList, WhiteListDTO.class);
    ApiResponse<WhiteListDTO> response = new ApiResponse<>(HttpStatus.OK, userWhitelistDTO, null);

    return ResponseEntity.status(200).body(response);
  }

  @PostMapping("/{productId}")
  public ResponseEntity<ApiResponse<WhiteListDTO>> addProduct(@AuthenticationPrincipal UserDetails user, @PathVariable String productId) {
    Whitelist userWhiteList = this.whitelistService.addProductToWhitelist(user.getUsername(), productId);
    WhiteListDTO userWhitelistDTO = modelMapper.map(userWhiteList, WhiteListDTO.class);
    ApiResponse<WhiteListDTO> response = new ApiResponse<>(HttpStatus.OK, userWhitelistDTO, null);

    return ResponseEntity.status(200).body(response);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<ApiResponse<WhiteListDTO>> removeProduct(@AuthenticationPrincipal UserDetails user, @PathVariable String productId) {
    Whitelist userWhiteList = this.whitelistService.removeProductFromWhitelist(user.getUsername(), productId);
    WhiteListDTO userWhitelistDTO = modelMapper.map(userWhiteList, WhiteListDTO.class);
    ApiResponse<WhiteListDTO> response = new ApiResponse<>(HttpStatus.OK, userWhitelistDTO, null);

    return ResponseEntity.status(200).body(response);
  }
}
