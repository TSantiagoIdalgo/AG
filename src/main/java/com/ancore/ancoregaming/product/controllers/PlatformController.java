package com.ancore.ancoregaming.product.controllers;

import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.product.model.Platform;
import com.ancore.ancoregaming.product.services.platform.IPlatformService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platform")
public class PlatformController {

  @Autowired
  private IPlatformService platformService;

  public ResponseEntity<ApiResponse<List<Platform>>> getAllPlatforms() {
    List<Platform> platforms = this.platformService.findAllPlatforms();
    ApiResponse<List<Platform>> response = new ApiResponse<>(HttpStatus.OK, platforms, null);
    return ResponseEntity.status(200).body(response);
  }
}
