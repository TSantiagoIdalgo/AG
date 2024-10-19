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
  private boolean cartIsPaid = false;

  @OneToOne
  private User user;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
  private List<CartItem> items;

  public Cart() {
  }

  public Cart(UUID id, BigDecimal total, User user, List<CartItem> items) {
    this.id = UUID.randomUUID();
    this.total = total;
    this.user = user;
    this.items = items;
  }

}
