package com.ancore.ancoregaming.cart.model;

import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column
  private BigDecimal total;

  @Column
  private BigDecimal subtotal;
  @OneToOne
  private User user;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
  private List<CartItem> items;

  public Cart() {
  }

  private Cart(Builder builder) {
    this.total = builder.total;
    this.subtotal = builder.subtotal;
    this.user = builder.user;
    this.items = builder.items;
  }

  public static class Builder {

    private BigDecimal total;
    private BigDecimal subtotal;
    private User user;
    private List<CartItem> items;

    public Builder setTotal(BigDecimal total) {
      this.total = total;
      return this;
    }

    public Builder setSubtotal(BigDecimal subtotal) {
      this.subtotal = subtotal;
      return this;
    }

    public Builder setUser(User user) {
      this.user = user;
      return this;
    }

    public Builder setItems(List<CartItem> items) {
      this.items = items;
      return this;
    }

    public Cart build() {
      return new Cart(this);
    }

  }

  @Override
  public String toString() {
    return "Cart{" + "id=" + id + ", total=" + total + ", subtotal=" + subtotal + ", items=" + items + '}';
  }

}
