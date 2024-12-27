package com.ancore.ancoregaming.product.repositories;

import com.ancore.ancoregaming.product.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlatformRepository extends JpaRepository<Platform, String> {
  Optional<Platform> findByNameAndPlatform(String name, String platform);
}
