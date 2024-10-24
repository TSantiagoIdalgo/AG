package com.ancore.ancoregaming.product.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class FilesDTO {

  @NotNull
  private MultipartFile mainImage;
  private MultipartFile trailer;
  @NotNull
  private MultipartFile backgroundImage;
  @NotNull
  private List<MultipartFile> images;

  public FilesDTO() {
  }

  public FilesDTO(MultipartFile mainImage, MultipartFile trailer, MultipartFile backgroundImage, List<MultipartFile> images) {
    this.mainImage = mainImage;
    this.trailer = trailer;
    this.backgroundImage = backgroundImage;
    this.images = images;
  }

  public MultipartFile getMainImage() {
    return mainImage;
  }

  public void setMainImage(MultipartFile mainImage) {
    this.mainImage = mainImage;
  }

  public MultipartFile getTrailer() {
    return trailer;
  }

  public void setTrailer(MultipartFile trailer) {
    this.trailer = trailer;
  }

  public MultipartFile getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(MultipartFile backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public List<MultipartFile> getImages() {
    return images;
  }

  public void setImages(List<MultipartFile> images) {
    this.images = images;
  }

}
