package com.ancore.ancoregaming.product.model;

import jakarta.persistence.Column;
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

  @Column
  private String platform;
  
  @Column
  private boolean disabled;

  public Platform() {
  }
  
  public Platform(String name, String platform, boolean disabled) {
    this.name = name;
    this.platform = platform;
    this.disabled = disabled;
  }
  
  @Override
  public String toString() {
    return "Platform{" +
        "name='" + name + '\'' +
        ", platform='" + platform + '\'' +
        ", disabled=" + disabled +
        '}';
  }
}
