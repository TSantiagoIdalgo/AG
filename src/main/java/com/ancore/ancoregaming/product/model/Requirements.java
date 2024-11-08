package com.ancore.ancoregaming.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private String OS;
  @Column
  private double memory;
  @Column
  private String graphics;
  @Column
  private double Directx_v;
  @Column
  private double storage;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Enumerated(EnumType.STRING)
  private RequirementType type;

}
