package com.ancore.ancoregaming.whitelist.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ancore.ancoregaming.whitelist.model.WhitelistItem;

@Repository
public interface IWhitelistItemRepository extends JpaRepository<WhitelistItem, UUID> {
    public WhitelistItem findByWhitelistIdAndProductId(UUID whitelistId, UUID productId);
}
