package com.ancore.ancoregaming.product.services.product;

import org.springframework.data.jpa.domain.Specification;

import com.ancore.ancoregaming.checkout.model.CheckoutItems;
import com.ancore.ancoregaming.product.model.Product;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class ProductSpecificationService {

    public static Specification<Product> hasName(String name) {
        return (root, query, builder) -> name == null ? builder.conjunction()
                : builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> hasFranchise(String franchise) {
        return (root, query, builder) -> franchise == null ? builder.conjunction()
                : builder.like(root.get("franchise"), "%" + franchise + "%");
    }

    public static Specification<Product> hasDeveloper(String developer) {
        return (root, query, builder) -> developer == null ? builder.conjunction()
                : builder.like(root.get("developer"), "%" + developer + "%");
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
}
