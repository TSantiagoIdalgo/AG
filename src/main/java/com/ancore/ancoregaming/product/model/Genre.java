package com.ancore.ancoregaming.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Genre {

  @Id
  private String name;
  @Column
  private int recommendedAge;

  public Genre(String name, int recommendedAge) {
    this.name = name;
    this.recommendedAge = recommendedAge;
  }

}
