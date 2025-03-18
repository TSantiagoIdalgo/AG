package com.ancore.ancoregaming.product.services.product;

import com.ancore.ancoregaming.checkout.model.CheckoutItems;
import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.whitelist.model.WhitelistItem;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecificationService {

  public static Specification<Product> hasFranchise(String franchise) {
    return (root, query, builder) -> franchise == null ? builder.conjunction()
        : builder.like(
            builder.lower(root.get("franchise")),
            "%" + franchise.toLowerCase() + "%");
  }

  public static Specification<Product> hasDeveloper(String developer) {
    return (root, query, builder) -> developer == null ? builder.conjunction()
        : builder.like(
            builder.lower(root.get("developer")),
            "%" + developer.toLowerCase() + "%");
  }

  public static Specification<Product> hasDistributor(String distributor) {
    return (root, query, builder) -> distributor == null ? builder.conjunction()
        : builder.like(
            builder.lower(root.get("distributor")),
            "%" + distributor.toLowerCase() + "%"
        );
  }

  public static Specification<Product> hasPriceRange(Double minPrice, Double maxPrice) {
    return (root, query, builder) -> {
      // Calcula el descuento como un porcentaje del precio
      Expression<Number> discountAmount = builder.prod(
          root.get("price"),
          builder.quot(root.get("discount"), 100.0)
      );

      // Calcula el precio final (price - discountAmount) y lo convierte a Double
      Expression<Double> finalPrice = builder.diff(
          root.get("price"),
          discountAmount
      ).as(Double.class); // Convierte el resultado a Double

      // Crea los predicados para el rango de precios
      Predicate min = minPrice != null
          ? builder.greaterThanOrEqualTo(finalPrice, minPrice)
          : builder.conjunction();
      Predicate max = maxPrice != null
          ? builder.lessThanOrEqualTo(finalPrice, maxPrice)
          : builder.conjunction();

      // Combina los predicados con AND
      return builder.and(min, max);
    };
  }

  public static Specification<Product> hasDiscountRange(Double minDiscount, Double maxDiscount) {
    return (root, query, builder) -> {
      Predicate min = minDiscount != null
          ? builder.greaterThanOrEqualTo(root.get("discount"), minDiscount)
          : builder.conjunction();
      Predicate max = maxDiscount != null
          ? builder.lessThanOrEqualTo(root.get("discount"), maxDiscount)
          : builder.conjunction();
      return builder.and(min, max);
    };
  }

  public static Specification<Product> orderByCheckoutCount(boolean orderByCheckoutCount) {
    return (root, query, criteriaBuilder) -> {
      if (orderByCheckoutCount && query != null) {
        Subquery<Long> checkoutCount = query.subquery(Long.class);
        Root<CheckoutItems> checkoutItemRoot = checkoutCount.from(CheckoutItems.class);
        checkoutCount.select(criteriaBuilder.countDistinct(checkoutItemRoot.get("cartItem")))
            .where(criteriaBuilder.equal(checkoutItemRoot.get("cartItem").get("product"), root));

        query.orderBy(criteriaBuilder.desc(checkoutCount));
      }
      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Product> orderByCreatedAt(boolean orderByCreatedAt) {
    return (root, query, criteriaBuilder) -> {
      if (orderByCreatedAt && query != null) {
        query.orderBy(criteriaBuilder.desc(root.get("release_date")));
      }
      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Product> orderByPrice(boolean orderByFinalPrice) {
    return (root, query, criteriaBuilder) -> {
      if (orderByFinalPrice && query != null) {
        // Calcula el descuento como un porcentaje del precio
        Expression<Number> discountAmount = criteriaBuilder.prod(
            root.get("price"),
            criteriaBuilder.quot(root.get("discount"), 100.0)
        );

        // Calcula el precio final (price - discountAmount) y lo convierte a Double
        Expression<Double> finalPrice = criteriaBuilder.diff(
            root.get("price"),
            discountAmount
        ).as(Double.class); // Convierte el resultado a Double

        // Ordena por el precio final de forma ascendente
        query.orderBy(criteriaBuilder.asc(finalPrice));
      }
      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Product> orderByDiscount(boolean orderByDiscount) {
    return (root, query, criteriaBuilder) -> {
      if (orderByDiscount && query != null) {
        query.orderBy(criteriaBuilder.desc(root.get("discount")));
      }
      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Product> orderByWishListCount(boolean orderByWishList) {
    return (root, query, criteriaBuilder) -> {
      if (orderByWishList && query != null) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<WhitelistItem> whitelistRoot = subquery.from(WhitelistItem.class);
        subquery.select(criteriaBuilder.count(whitelistRoot))
            .where(criteriaBuilder.equal(whitelistRoot.get("product"), root));
        query.orderBy(criteriaBuilder.desc(subquery));
      }

      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Product> orderByRecommendationCount(boolean orderByRecommendation) {
    return (root, query, criteriaBuilder) -> {
      if (orderByRecommendation && query != null) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Review> reviewRoot = subquery.from(Review.class);
        subquery.select(criteriaBuilder.count(reviewRoot))
            .where(
                criteriaBuilder.equal(reviewRoot.get("product"), root),
                criteriaBuilder.isTrue(reviewRoot.get("recommended")));
        query.orderBy(criteriaBuilder.desc(subquery));
      }

      return criteriaBuilder.conjunction();
    };
  }

  public static Specification<Product> hasKeyword(String keyword) {
    return (root, query, criteriaBuilder) -> {
      if (keyword == null || keyword.isBlank()) {
        return criteriaBuilder.conjunction();
      }
      Predicate namePredicate = criteriaBuilder.like(
          criteriaBuilder.lower(root.get("name")),
          "%" + keyword.toLowerCase() + "%"
      );

      Join<Product, String> tagsJoin = root.join("tags");
      Predicate tagPredicate = criteriaBuilder.like(
          criteriaBuilder.lower(tagsJoin),
          "%" + keyword.toLowerCase() + "%"
      );

      return criteriaBuilder.or(namePredicate, tagPredicate);
    };
  }

  public static Specification<Product> hasGenres(List<String> genreNames) {
    return (root, query, criteriaBuilder) -> {
      if (genreNames == null || genreNames.isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      Join<Product, Genre> genreJoin = root.join("genres");

      query.groupBy(root.get("id"))
          .having(criteriaBuilder.equal(criteriaBuilder.countDistinct(genreJoin.get("name")), genreNames.size()));

      return genreJoin.get("name").in(genreNames);
    };
  }

  public static Specification<Product> hasPlatform(List<String> platformNames) {
    return (root, query, criteriaBuilder) -> {
      if (platformNames == null || platformNames.isEmpty()) {
        return criteriaBuilder.conjunction();
      }

      Join<Product, Platform> platformJoin = root.join("platforms");
      List<Predicate> predicates = platformNames.stream()
          .map(genre -> criteriaBuilder.equal(
          criteriaBuilder.lower(platformJoin.get("name")),
          genre.toLowerCase()))
          .toList();
      return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<Product> hasSystem(List<String> systemNames) {
    return (root, query, criteriaBuilder) -> {
      if (systemNames == null || systemNames.isEmpty()) {
        return criteriaBuilder.conjunction();
      }

      Join<Product, Platform> platformJoin = root.join("platforms");
      List<Predicate> predicates = systemNames.stream()
          .map(genre -> criteriaBuilder.equal(
          criteriaBuilder.lower(platformJoin.get("platform")),
          genre.toLowerCase()))
          .toList();
      return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    };
  }
}
