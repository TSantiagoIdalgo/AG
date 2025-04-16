package com.ancore.ancoregaming.checkout.repositories;

import com.ancore.ancoregaming.checkout.model.Checkout;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICheckoutRepository extends JpaRepository<Checkout, UUID> {
  @Query("SELECT c FROM Checkout c WHERE c.user.email = :email ORDER BY c.createdAt DESC")
  public List<Checkout> findByUserEmail(String email);
}
