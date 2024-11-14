package com.ancore.ancoregaming.checkout.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ancore.ancoregaming.checkout.model.CheckoutItems;

@Repository
public interface ICheckoutItemsRepository extends JpaRepository<CheckoutItems, Long> {

}
