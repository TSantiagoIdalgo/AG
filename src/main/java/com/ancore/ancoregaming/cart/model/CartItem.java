package com.ancore.ancoregaming.cart.model;

import com.ancore.ancoregaming.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "cart_id", nullable = false)
  private Cart cart;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column
  private int cuantity;
  @Column
  private double price;

  public CartItem() {
  }

  public CartItem(UUID id, Cart cart, Product product, int cuantity, double price) {
    this.id = UUID.randomUUID();
    this.cart = cart;
    this.product = product;
    this.cuantity = cuantity;
    this.price = price;
  }

}
