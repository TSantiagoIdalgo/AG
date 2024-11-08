package com.ancore.ancoregaming.product.repositories;

import com.ancore.ancoregaming.product.model.Requirements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRequirementsRepository extends JpaRepository<Requirements, Long> {

}
