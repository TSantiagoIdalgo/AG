package com.ancore.ancoregaming.whitelist.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ancore.ancoregaming.whitelist.model.Whitelist;

@Repository
public interface IWhitelistRepository extends JpaRepository<Whitelist, UUID> {

  public Whitelist findByUserEmail(String userId);

  @Query("SELECT w FROM Whitelist w JOIN w.whitelistItems wi WHERE w.user.email = :userId AND wi.product.id = :productId")
  Optional<Whitelist> findByUserIdAndProductId(@Param("userId") String userId, @Param("productId") UUID productId);
}
