package com.ancore.ancoregaming.cart.components;

import com.ancore.ancoregaming.cart.services.ICartService;
import com.ancore.ancoregaming.product.services.product.IProductService;
import com.ancore.ancoregaming.user.services.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {
    private final ICartService cartService;
    private final IUserService userService;
    private final IProductService productService;

    @Autowired
    public CommandFactory(ICartService cartService, IUserService userService, IProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }

    public AddProductCommand createAddProductCommand(String productId, UserDetails userDetails) {
        return new AddProductCommand(productId, userDetails, userService, productService, cartService);
    }

    public RemoveProductCommand createRemoveProductCommand(String productId, UserDetails userDetails) {
        return new RemoveProductCommand(productId, userDetails, userService, productService, cartService);
    }

    public DecreaseProductCommand createDecreaseProductCommand(String productId, UserDetails userDetails) {
        return new DecreaseProductCommand(productId, userDetails, userService, productService, cartService);
    }
}
