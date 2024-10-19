package com.ancore.ancoregaming.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Platform {

  @Id
  private String name;

  public Platform(String name) {
    this.name = name;
  }

}
