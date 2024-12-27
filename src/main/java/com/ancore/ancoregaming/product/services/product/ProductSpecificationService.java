package com.ancore.ancoregaming.product.services.product;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ancore.ancoregaming.checkout.model.CheckoutItems;
import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.model.Platform;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.review.model.Review;
import com.ancore.ancoregaming.whitelist.model.WhitelistItem;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class ProductSpecificationService {

    public static Specification<Product> hasName(String name) {
        return (root, query, builder) -> {
            if (name == null) {
                return builder.conjunction();
            }
            return builder.like(
                    builder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

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

    public static Specification<Product> hasPriceRange(Double minPrice, Double maxPrice) {
        return (root, query, builder) -> {
            Predicate min = minPrice != null
                    ? builder.greaterThanOrEqualTo(root.get("price"), minPrice)
                    : builder.conjunction();
            Predicate max = maxPrice != null ? builder.lessThanOrEqualTo(root.get("price"), maxPrice)
                    : builder.conjunction();
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

    public static Specification<Product> hasTags(List<String> tags) {
        return (root, query, criteriaBuilder) -> {
            if (tags == null || tags.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Product, String> tagsJoin = root.join("tags");
            List<Predicate> predicates = tags.stream()
                    .map(tag -> criteriaBuilder.like(
                            criteriaBuilder.lower(tagsJoin),
                            "%" + tag.toLowerCase() + "%"))
                    .toList();
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> hasGenres(List<String> genreNames) {
        return (root, query, criteriaBuilder) -> {
            if (genreNames == null || genreNames.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Product, Genre> genreJoin = root.join("genres");
            List<Predicate> predicates = genreNames.stream()
                    .map(genre -> criteriaBuilder.equal(
                            criteriaBuilder.lower(genreJoin.get("name")),
                            genre.toLowerCase()))
                    .toList();
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
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
}
