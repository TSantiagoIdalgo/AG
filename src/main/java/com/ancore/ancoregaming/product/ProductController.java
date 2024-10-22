package com.ancore.ancoregaming.product;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(("/product"))
@RequiredArgsConstructor
public class ProductController {

  @Value("${CLOUDINARY_URL}")
  private String cloudinaryUrl;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/")
  public ResponseEntity<String> createProduct(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      return new ResponseEntity<>("Por favor selecciona un archivo", HttpStatus.BAD_REQUEST);
    }
    try {
      // Similar a NodeJS, transforma el archivo en un buffer de bytes antes de enviarlo
      Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
      Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

      String imageUrl = uploadResult.get("url").toString();

      return new ResponseEntity<>("Archivo subido con éxito: " + imageUrl, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>("Error al subir el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
