package com.ancore.ancoregaming.whitelist.model;

import java.util.List;
import java.util.UUID;

import com.ancore.ancoregaming.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

  @OneToMany(mappedBy = "whitelist")
  private List<WhitelistItem> whitelistItems;

  public Whitelist() {
  }

  public Whitelist(User user, List<WhitelistItem> whitelistItems) {
    this.id = UUID.randomUUID();
    this.user = user;
    this.whitelistItems = whitelistItems;
  }

  @Override
  public String toString() {
    return "Whitelist{" + "id=" + id + ", user=" + user + ", whitelistItems=" + whitelistItems + '}';
  }

}
