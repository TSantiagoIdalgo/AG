package com.ancore.ancoregaming.product.model;

import com.ancore.ancoregaming.review.model.Review;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(length = 128)
  private String name;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "product_platforms",
          joinColumns = @JoinColumn(name = "product_id"),
          inverseJoinColumns = @JoinColumn(name = "platform_name")
  )
  private List<Platform> platforms;

  @Column(length = 50, nullable = false)
  private String developer;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "product_genres",
          joinColumns = @JoinColumn(name = "product_id"),
          inverseJoinColumns = @JoinColumn(name = "genre_name")
  )
  private List<Genre> genres;

  @Column
  private boolean disabled;

  @Column
  private int stock;

  @Column
  private BigDecimal price;

  @Column
  private String mainImage;

  @Column
  private String backgroundImage;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
  private List<String> images;

  @Column
  private BigDecimal discount;

  @OneToMany(mappedBy = "product")
  private List<Review> reviews;

  public Product() {
  }

  public Product(UUID id, String name, List<Platform> platforms, String developer, List<Genre> genres, boolean disabled, int stock, BigDecimal price, String mainImage, String backgroundImage, List<String> images, BigDecimal discount) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.platforms = platforms;
    this.developer = developer;
    this.genres = genres;
    this.disabled = disabled;
    this.stock = stock;
    this.price = price;
    this.mainImage = mainImage;
    this.backgroundImage = backgroundImage;
    this.images = images;
    this.discount = discount;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 11 * hash + Objects.hashCode(this.id);
    hash = 11 * hash + Objects.hashCode(this.name);
    hash = 11 * hash + Objects.hashCode(this.platforms);
    hash = 11 * hash + Objects.hashCode(this.developer);
    hash = 11 * hash + Objects.hashCode(this.genres);
    hash = 11 * hash + (this.disabled ? 1 : 0);
    hash = 11 * hash + this.stock;
    hash = 11 * hash + Objects.hashCode(this.price);
    hash = 11 * hash + Objects.hashCode(this.mainImage);
    hash = 11 * hash + Objects.hashCode(this.backgroundImage);
    hash = 11 * hash + Objects.hashCode(this.images);
    hash = 11 * hash + Objects.hashCode(this.discount);
    hash = 11 * hash + Objects.hashCode(this.reviews);
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
    final Product other = (Product) obj;
    if (this.disabled != other.disabled) {
      return false;
    }
    if (this.stock != other.stock) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.developer, other.developer)) {
      return false;
    }
    if (!Objects.equals(this.mainImage, other.mainImage)) {
      return false;
    }
    if (!Objects.equals(this.backgroundImage, other.backgroundImage)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.platforms, other.platforms)) {
      return false;
    }
    if (!Objects.equals(this.genres, other.genres)) {
      return false;
    }
    if (!Objects.equals(this.price, other.price)) {
      return false;
    }
    if (!Objects.equals(this.images, other.images)) {
      return false;
    }
    if (!Objects.equals(this.discount, other.discount)) {
      return false;
    }
    return Objects.equals(this.reviews, other.reviews);
  }

  @Override
  public String toString() {
    return "Product{" + "id=" + id + ", name=" + name + ", platforms=" + platforms + ", developer=" + developer + ", genres=" + genres + ", disabled=" + disabled + ", stock=" + stock + ", price=" + price + ", mainImage=" + mainImage + ", backgroundImage=" + backgroundImage + ", images=" + images + ", discount=" + discount + ", reviews=" + reviews + '}';
  }

}
