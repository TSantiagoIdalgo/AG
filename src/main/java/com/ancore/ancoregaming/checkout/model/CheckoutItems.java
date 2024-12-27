package com.ancore.ancoregaming.checkout.model;

import com.ancore.ancoregaming.cart.model.CartItem;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CheckoutItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;

    @ManyToOne
    @JoinColumn(name = "checkout_id", nullable = false)
    private Checkout checkout;

    public CheckoutItems() {
    }

    public CheckoutItems(CartItem cartItem, Checkout checkout) {
        this.cartItem = cartItem;
        this.checkout = checkout;
    }
}
