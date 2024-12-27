package com.ancore.ancoregaming.cart.model;

import com.ancore.ancoregaming.checkout.model.CheckoutItems;
import com.ancore.ancoregaming.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.List;
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
  private int quantity;
  @Column
  private BigDecimal total;
  @Column
  private BigDecimal subtotal;
  @Column
  private boolean itemIsPaid;

  @OneToMany(mappedBy = "cartItem")
  private List<CheckoutItems> checkoutItems;

  public CartItem() {
  }

  private CartItem(Builder builder) {
    this.cart = builder.cart;
    this.product = builder.product;
    this.quantity = builder.cuantity;
    this.total = builder.total;
    this.subtotal = builder.subtotal;
    this.itemIsPaid = builder.itemIsPaid;
  }

  public static class Builder {

    private Cart cart;
    private Product product;
    private int cuantity;
    private BigDecimal total;
    private BigDecimal subtotal;
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

    public Builder setTotal(BigDecimal total) {
      this.total = total;
      return this;
    }

    public Builder setSubtotal(BigDecimal subtotal) {
      this.subtotal = subtotal;
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

  @Override
  public String toString() {
    return "CartItem{" + "id=" + id + ", product=" + product + ", cuantity=" + quantity + ", total=" + total
        + ", subtotal=" + subtotal + ", itemIsPaid=" + itemIsPaid + '}';
  }

}
