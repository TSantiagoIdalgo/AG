package com.ancore.ancoregaming.email.dtos;

import com.ancore.ancoregaming.cart.model.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PurchaseEmailDTO {

    private String addressee;
    private String username;
    private BigDecimal total;
    private List<CartItem> items;
    private String productsQuantity;
}
