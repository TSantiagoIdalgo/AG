package com.ancore.ancoregaming.cart.services;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.product.model.Product;

public interface ICartItemService {
    public CartItem findOrCreateCartItem(Cart cart, Product product);

    public void incrementCartItem(Cart cart, CartItem item, Product product);

    public void decreaseCartItem(Cart cart, CartItem item, Product product);
}