package com.ancore.ancoregaming.product.repositories;

import com.ancore.ancoregaming.product.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

}
