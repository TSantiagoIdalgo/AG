package com.ancore.ancoregaming.whitelist.model;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Whitelist {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "user_email", nullable = false, unique = true)
  private User user;

  @ManyToMany
  @JoinTable(
          name = "whitelist_products",
          joinColumns = @JoinColumn(name = "whitelist_id"),
          inverseJoinColumns = @JoinColumn(name = "product_id")
  )
  private List<Product> products;

  public Whitelist() {
  }

  public Whitelist(User user, List<Product> products) {
    this.id = UUID.randomUUID();
    this.user = user;
    this.products = products;
  }

  @Override
  public String toString() {
    return "Whitelist{" + "id=" + id + ", user=" + user + ", products=" + products + '}';
  }

}
