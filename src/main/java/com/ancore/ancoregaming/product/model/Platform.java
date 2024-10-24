package com.ancore.ancoregaming.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Platform {

  @Id
  private String name;

  @Column
  private boolean disabled;

  public Platform() {
  }

  public Platform(String name, boolean disabled) {
    this.name = name;
    this.disabled = disabled;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  @Override
  public String toString() {
    return "Platform{" + "name=" + name + ", disabled=" + disabled + '}';
  }

}
