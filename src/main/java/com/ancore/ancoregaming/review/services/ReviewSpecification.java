package com.ancore.ancoregaming.review.services;

import org.springframework.data.jpa.domain.Specification;

import com.ancore.ancoregaming.review.model.Review;

public class ReviewSpecification {
    public static Specification<Review> orderByCreatedAt(boolean orderByCreatedAt) {
        return (root, query, criteriaBuilder) -> {
            if (orderByCreatedAt && query != null) {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            }
            return criteriaBuilder.conjunction();
        };
    }
}
