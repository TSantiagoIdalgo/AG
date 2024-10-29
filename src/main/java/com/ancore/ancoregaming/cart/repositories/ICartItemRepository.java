package com.ancore.ancoregaming.cart.repositories;

import com.ancore.ancoregaming.cart.model.CartItem;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartItemRepository extends JpaRepository<CartItem, UUID> {

  public CartItem findByCartId(UUID cartId);

  public Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);

}
