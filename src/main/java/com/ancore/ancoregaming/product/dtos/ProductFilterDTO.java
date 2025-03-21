package com.ancore.ancoregaming.product.dtos;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterDTO {

  private int pageSize;
  private int pageNumber;
  private String developer;
  private String franchise;
  private Double minPrice;
  private Double maxPrice;
  private Double minDiscount;
  private Double maxDiscount;
  private List<String> tags;
  private List<String> genres;
  private List<String> platform;
  private List<String> system;
  private String name;
  private LocalDate release_date;
  private String distributor;
  private String pegi;
  private boolean orderByWishList;
  private boolean orderByRecommendation;
  private boolean orderByCheckoutCount;
  private boolean orderByCreatedAt;
  private boolean orderByPrice;
  private boolean orderByDiscount;

  @Override
  public String toString() {
    return "ProductFilterDTO{"
        + "pageSize=" + pageSize
        + ", pageNumber=" + pageNumber
        + ", developer='" + developer + '\''
        + ", franchise='" + franchise + '\''
        + ", minPrice=" + minPrice
        + ", maxPrice=" + maxPrice
        + ", minDiscount=" + minDiscount
        + ", maxDiscount=" + maxDiscount
        + ", genres=" + genres
        + ", platform=" + platform
        + ", name='" + name + '\''
        + ", release_date=" + release_date
        + ", distributor='" + distributor + '\''
        + ", pegi='" + pegi + '\''
        + ", orderByWishList=" + orderByWishList
        + ", orderByRecommendation=" + orderByRecommendation
        + ", orderByCheckoutCount=" + orderByCheckoutCount
        + '}';
  }
}
