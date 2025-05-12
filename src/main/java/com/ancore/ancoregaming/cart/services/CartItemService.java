package com.ancore.ancoregaming.cart.services;

import com.ancore.ancoregaming.cart.model.Cart;
import com.ancore.ancoregaming.cart.model.CartItem;
import com.ancore.ancoregaming.cart.repositories.ICartItemRepository;
import com.ancore.ancoregaming.checkout.model.Checkout;
import com.ancore.ancoregaming.checkout.model.CheckoutItems;
import com.ancore.ancoregaming.checkout.repositories.ICheckoutItemsRepository;
import com.ancore.ancoregaming.checkout.repositories.ICheckoutRepository;
import com.ancore.ancoregaming.product.model.Product;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.ParameterOutOfBoundsException;
import org.springframework.stereotype.Service;

@Service
public class CartItemService implements ICartItemService {
    private final ICartItemRepository cartItemRepository;
    private final ICheckoutRepository checkoutRepository;
    private final ICheckoutItemsRepository checkoutItemsRepository;
    
    @Autowired
    public CartItemService(ICartItemRepository cartItemRepository, ICheckoutRepository checkoutRepository, ICheckoutItemsRepository checkoutItemsRepository) {
        this.cartItemRepository = cartItemRepository;
        this.checkoutRepository = checkoutRepository;
        this.checkoutItemsRepository = checkoutItemsRepository;
    }
    
    @Override
    public CartItem findOrCreateCartItem(Cart cart, Product product) throws BadRequestException {
        if (product.getStock() == 0) {
            throw new BadRequestException("OUT_OF_STOCK");
        }
        CartItem cartItem = cartItemRepository.findUnpaidCartItemByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(() -> createItem(cart, product));
        
        return cartItem.isItemIsPaid() ? createItem(cart, product) : cartItem;
    }

    @Override
    public void incrementCartItem(Cart cart, CartItem item, Product product) throws BadRequestException {
        if (item.getQuantity() >= product.getStock()) {
            throw new BadRequestException("Stock exceed");
        }
        item.setQuantity(item.getQuantity() + 1);

        item.setSubtotal(item.getSubtotal().add(product.getPrice()));
        item.setTotal(item.getTotal().add(getFinalPrice(product.getPrice(), product.getDiscount())));

        cart.setSubtotal(cart.getSubtotal().add(product.getPrice()));
        cart.setTotal(cart.getTotal().add(getFinalPrice(product.getPrice(), product.getDiscount())));
    }

    @Override
    public void decreaseCartItem(Cart cart, CartItem item, Product product) {
        int newQuantity = item.getQuantity() - 1;
        if (newQuantity <= 0) {
            List<CheckoutItems> checkout = this.checkoutItemsRepository.findByCartItem(item);
            checkout.forEach(this.checkoutItemsRepository::delete);
            cart.getItems().remove(item); // Elimina el CartItem del Cart
            product.getCartItems().remove(item);
            cart.setSubtotal(cart.getSubtotal().subtract(product.getPrice()));
            cart.setTotal(cart.getTotal().subtract(getFinalPrice(product.getPrice(), product.getDiscount())));
            this.cartItemRepository.deleteById(item.getId());
        } else {
            item.setQuantity(newQuantity);
            item.setSubtotal(item.getSubtotal().subtract(product.getPrice()));
            item.setTotal(item.getTotal().subtract(getFinalPrice(product.getPrice(), product.getDiscount())));

            cart.setSubtotal(cart.getSubtotal().subtract(product.getPrice()));
            cart.setTotal(cart.getTotal().subtract(getFinalPrice(product.getPrice(), product.getDiscount())));
        }
    }

    private BigDecimal getFinalPrice(BigDecimal amount, BigDecimal d) {
        BigDecimal percentage = new BigDecimal("100");
        BigDecimal discount = percentage.subtract(d), multiplyAmount = amount.multiply(discount);
        return multiplyAmount.divide(percentage);
    }

    private CartItem createItem(Cart userCart, Product product) {
        CartItem newCartItem = new CartItem.Builder()
                .setCart(userCart)
                .setCuantity(0)
                .setTotal(BigDecimal.ZERO)
                .setSubtotal(BigDecimal.ZERO)
                .setProduct(product)
                .setItemIsPaid(false)
                .build();
        userCart.getItems().add(newCartItem);
        product.getCartItems().add(newCartItem);
        
        return newCartItem;
    }
}
