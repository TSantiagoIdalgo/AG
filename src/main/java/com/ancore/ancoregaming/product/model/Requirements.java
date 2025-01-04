package com.ancore.ancoregaming.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Requirements {

  public enum RequirementType {
    RECOMMENDED, MINIMUM
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  @Column
  private String os;
  @Column
  private double memory;
  @Column
  private String graphics;
  @Column
  private double directx_v;
  @Column
  private double storage;
  @Column
  private String processor;

  @Enumerated(EnumType.STRING)
  private RequirementType type;

  public Requirements() {
  }

  private Requirements(Builder builder) {
    this.os = builder.os;
    this.memory = builder.memory;
    this.graphics = builder.graphics;
    this.directx_v = builder.directx_v;
    this.storage = builder.storage;
    this.type = builder.type;
    this.processor = builder.processor;
  }

  public static class Builder {

    private String os;
    private double memory;
    private String graphics;
    private double directx_v;
    private double storage;
    private RequirementType type;
    private String processor;

    public Builder setOS(String OS) {
      this.os = OS;
      return this;
    }

    public Builder setMemory(double memory) {
      this.memory = memory;
      return this;
    }

    public Builder setGraphics(String graphics) {
      this.graphics = graphics;
      return this;
    }

    public Builder setProcessor(String processor) {
      this.processor = processor;
      return this;
    }

    public Builder setDirectx_v(double Directx_v) {
      this.directx_v = Directx_v;
      return this;
    }

    public Builder setStorage(double storage) {
      this.storage = storage;
      return this;
    }

    public Builder setType(RequirementType type) {
      this.type = type;
      return this;
    }

    public Requirements build() {
      return new Requirements(this);
    }
  }

  @Override
  public String toString() {
    return "Requirements{" + "id=" + id + ", OS=" + os + ", memory=" + memory + ", graphics=" + graphics + ", Directx_v=" + directx_v + ", storage=" + storage + ", type=" + type + '}';
  }

}
