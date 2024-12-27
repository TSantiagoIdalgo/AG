package com.ancore.ancoregaming.product.model;

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

  public Genre() {
  }

  public Genre(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Genre{" + "name=" + name + '}';
  }

}
