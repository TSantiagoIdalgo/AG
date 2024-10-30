package com.ancore.ancoregaming.whitelist.repositories;

import com.ancore.ancoregaming.whitelist.model.Whitelist;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWhitelistRepository extends JpaRepository<Whitelist, UUID> {

}
