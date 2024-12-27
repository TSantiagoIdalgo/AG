package com.ancore.ancoregaming.user.model;

import java.util.List;
import java.util.Objects;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.checkout.model.Checkout;
import com.ancore.ancoregaming.checkout.model.StockReservation;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.whitelist.model.Whitelist;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

  @Column(unique = true, length = 50, nullable = false)
  private String username;

  @Id
  @Column(unique = true, length = 128, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column
  private boolean verify;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Whitelist whitelist;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Cart cart;

  @OneToMany(mappedBy = "user")
  private List<Review> reviews;

  @OneToMany(mappedBy = "user")
  private List<StockReservation> stockReservation;

  @OneToMany(mappedBy = "user")
  private List<Checkout> checkouts;

  public User() {
  }

  public User(String username, String email, String password, boolean verify, List<Role> roles) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.verify = verify;
    this.roles = roles;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.username);
    hash = 37 * hash + Objects.hashCode(this.email);
    hash = 37 * hash + Objects.hashCode(this.password);
    hash = 37 * hash + (this.verify ? 1 : 0);
    hash = 37 * hash + Objects.hashCode(this.roles);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User) obj;
    if (this.verify != other.verify) {
      return false;
    }
    if (!Objects.equals(this.username, other.username)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.password, other.password)) {
      return false;
    }
    return this.roles == other.roles;
  }

  @Override
  public String toString() {
    return "User{" + "username=" + username + ", email=" + email + ", password=" + password + ", verify=" + verify
        + ", role=" + roles + '}';
  }

}
