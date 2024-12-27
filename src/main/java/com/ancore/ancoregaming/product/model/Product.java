package com.ancore.ancoregaming.product.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.ancore.ancoregaming.checkout.model.StockReservation;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.whitelist.model.WhitelistItem;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.Version;
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

  @Column(columnDefinition = "TEXT")
  private String description;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "product_platforms", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "platform_name"))
  private List<Platform> platforms;

  @Column(length = 50, nullable = false)
  private String developer;

  @Column
  private String franchise;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "product_genres", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "genre_name"))
  private List<Genre> genres;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "product_requirements", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "requirement_id"))
  private List<Requirements> requirements;

  @ElementCollection
  @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
  private List<String> tags;

  @Column
  private boolean disabled;

  @Column
  private int stock;

  @Column
  private BigDecimal price;

  @Column
  private String mainImage;

  @Column
  String trailer;

  @Column
  private String backgroundImage;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
  private List<String> images;

  @Column
  private BigDecimal discount;

  @OneToMany(mappedBy = "product")
  private List<Review> reviews;

  @OneToMany(mappedBy = "product")
  private List<WhitelistItem> whitelistItems;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<StockReservation> stockReservation;

  @Version
  private Long version;

  public Product() {
  }

  private Product(Builder builder) {
    this.name = builder.name;
    this.description = builder.description;
    this.platforms = builder.platforms;
    this.developer = builder.developer;
    this.franchise = builder.franchise;
    this.genres = builder.genres;
    this.requirements = builder.requirements;
    this.tags = builder.tags;
    this.disabled = builder.disabled;
    this.stock = builder.stock;
    this.price = builder.price;
    this.mainImage = builder.mainImage;
    this.trailer = builder.trailer;
    this.backgroundImage = builder.backgroundImage;
    this.images = builder.images;
    this.discount = builder.discount;
    this.reviews = builder.reviews;
  }

  public static class Builder {

    private String name;
    private String description;
    private List<Platform> platforms;
    private String developer;
    private String franchise;
    private List<Genre> genres;
    private List<Requirements> requirements;
    private List<String> tags;
    private boolean disabled;
    private int stock;
    private BigDecimal price;
    private String mainImage;
    private String trailer;
    private String backgroundImage;
    private List<String> images;
    private BigDecimal discount;
    private List<Review> reviews;

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setPlatforms(List<Platform> platforms) {
      this.platforms = platforms;
      return this;
    }

    public Builder setDeveloper(String developer) {
      this.developer = developer;
      return this;
    }

    public Builder setRequirements(List<Requirements> requirements) {
      this.requirements = requirements;
      return this;
    }

    public Builder setFranchise(String franchise) {
      this.franchise = franchise;
      return this;
    }

    public Builder setGenres(List<Genre> genres) {
      this.genres = genres;
      return this;
    }

    public Builder setDisabled(boolean disabled) {
      this.disabled = disabled;
      return this;
    }

    public Builder setStock(int stock) {
      this.stock = stock;
      return this;
    }

    public Builder setPrice(BigDecimal price) {
      this.price = price;
      return this;
    }

    public Builder setMainImage(String mainImage) {
      this.mainImage = mainImage;
      return this;
    }

    public Builder setTrailer(String trailerUrl) {
      this.trailer = trailerUrl;
      return this;
    }

    public Builder setBackgroundImage(String backgroundImage) {
      this.backgroundImage = backgroundImage;
      return this;
    }

    public Builder setImages(List<String> images) {
      this.images = images;
      return this;
    }

    public Builder setDiscount(BigDecimal discount) {
      this.discount = discount;
      return this;
    }

    public Builder setReviews(List<Review> reviews) {
      this.reviews = reviews;
      return this;
    }

    public Builder setTags(List<String> tags) {
      this.tags = tags;
      return this;
    }

    public Product build() {
      return new Product(this);
    }

  }

  @Override
  public String toString() {
    return "Product{" + "id=" + id + ", name=" + name + ", description=" + description + ", platforms=" + platforms
        + ", developer=" + developer + ", genres=" + genres + ", tags=" + tags + ", disabled=" + disabled + ", stock="
        + stock + ", price=" + price + ", mainImage=" + mainImage + ", trailer=" + trailer + ", backgroundImage="
        + backgroundImage + ", images=" + images + ", discount=" + discount + ", reviews=" + reviews + '}';
  }

}
