package com.ancore.ancoregaming.product.dtos;

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
    private List<String> genres;
    private List<String> tags;
    private List<String> platform;
    private String name;
    private boolean orderByWishList;
    private boolean orderByRecommendation;
    private boolean orderByCheckoutCount;
}
