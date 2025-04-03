package com.ancore.ancoregaming.cart.components;

import com.ancore.ancoregaming.cart.interfaces.Command;
import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.services.ICartService;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.IProductService;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.services.user.IUserService;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;

public class AddProductCommand implements Command<Cart> {
    private final ICartService cartService;
    private final User user;
    private final Product product;

    private Cart result;

    public AddProductCommand(String productId, UserDetails userDetails, IUserService userService,
            IProductService productService, ICartService cartService) {
        this.product = productService.findProduct(productId);
        this.user = userService.findUser(userDetails.getUsername());
        this.cartService = cartService;
    }

    @Override
    public Cart execute() throws BadRequestException {
        this.result = this.cartService.increaseProducts(user, product);
        return result;
    }

    @Override
    public Cart undo() {
        this.result = this.cartService.decreaseProduct(user, product);
        return result;
    }

    @Override
    public Cart getResult() {
        return this.result;
    }

}
