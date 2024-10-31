package com.ancore.ancoregaming.whitelist.repositories;

import com.ancore.ancoregaming.whitelist.model.Whitelist;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWhitelistRepository extends JpaRepository<Whitelist, UUID> {

  public Whitelist findByUserId(String userId);
}
