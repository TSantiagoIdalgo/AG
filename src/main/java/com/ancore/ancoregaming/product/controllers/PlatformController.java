package com.ancore.ancoregaming.product.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.product.model.Platform;
import com.ancore.ancoregaming.product.services.platform.IPlatformService;

@RestController
@RequestMapping("/platform")
public class PlatformController {
  private final IPlatformService platformService;
  @Autowired
  public PlatformController(IPlatformService platformService) {
    this.platformService = platformService;
  }
  @GetMapping("/")
  public ApiEntityResponse<List<Platform>> getAllPlatforms() {
    List<Platform> platforms = this.platformService.findAllPlatforms();
    ApiResponse<List<Platform>> response = new ApiResponse<>(platforms, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
}
