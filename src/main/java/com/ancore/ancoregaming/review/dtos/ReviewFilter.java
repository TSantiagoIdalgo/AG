package com.ancore.ancoregaming.review.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFilter {
    private int pageSize;
    private int pageNumber;
    private boolean orderByCreatedAt;

    public ReviewFilter() {
    }

    public ReviewFilter(int pageSize, int pageNumber, boolean orderByCreatedAt) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.orderByCreatedAt = orderByCreatedAt;
    }

}
