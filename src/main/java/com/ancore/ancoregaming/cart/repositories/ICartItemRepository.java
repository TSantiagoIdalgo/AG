package com.ancore.ancoregaming.cart.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ancore.ancoregaming.cart.model.CartItem;

public interface ICartItemRepository extends JpaRepository<CartItem, UUID> {

    public CartItem findByCartId(UUID cartId);

    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c JOIN ci.product p WHERE c.id = :cartId AND p.id = :productId AND ci.itemIsPaid = false AND ci.paymentStatus IS NULL")
    Optional<CartItem> findUnpaidCartItemByCartIdAndProductId(@Param("cartId") UUID cartId,
            @Param("productId") UUID productId);

    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c JOIN ci.product p WHERE c.id = :cartId AND p.id = :productId AND ci.itemIsPaid = true AND ci.paymentStatus IS NULL")
    List<CartItem> findPaidCartItemByCartIdAndProductId(@Param("cartId") UUID cartId,
            @Param("productId") UUID productId);

}
