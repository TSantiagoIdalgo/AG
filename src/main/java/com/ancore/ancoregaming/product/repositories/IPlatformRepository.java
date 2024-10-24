package com.ancore.ancoregaming.product.repositories;

import com.ancore.ancoregaming.product.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlatformRepository extends JpaRepository<Platform, String> {

}
