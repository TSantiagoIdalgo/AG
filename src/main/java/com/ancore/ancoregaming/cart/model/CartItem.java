package com.ancore.ancoregaming.cart.model;

import com.ancore.ancoregaming.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
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
  private BigDecimal price;
  @Column
  private boolean itemIsPaid;

  public CartItem() {
  }

  private CartItem(Builder builder) {
    this.cart = builder.cart;
    this.product = builder.product;
    this.cuantity = builder.cuantity;
    this.price = builder.price;
    this.itemIsPaid = builder.itemIsPaid;
  }

  public static class Builder {

    private Cart cart;
    private Product product;
    private int cuantity;
    private BigDecimal price;
    private boolean itemIsPaid;

    public Builder setCart(Cart cart) {
      this.cart = cart;
      return this;
    }

    public Builder setProduct(Product product) {
      this.product = product;
      return this;
    }

    public Builder setCuantity(int cuantity) {
      this.cuantity = cuantity;
      return this;
    }

    public Builder setPrice(BigDecimal price) {
      this.price = price;
      return this;
    }

    public Builder setItemIsPaid(boolean itemIsPaid) {
      this.itemIsPaid = itemIsPaid;
      return this;
    }

    public CartItem build() {
      return new CartItem(this);
    }
  }
}
