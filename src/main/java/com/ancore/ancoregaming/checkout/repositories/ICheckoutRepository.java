package com.ancore.ancoregaming.checkout.repositories;

import com.ancore.ancoregaming.checkout.model.Checkout;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICheckoutRepository extends JpaRepository<Checkout, UUID> {

}
