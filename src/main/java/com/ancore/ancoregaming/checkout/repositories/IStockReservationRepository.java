package com.ancore.ancoregaming.checkout.repositories;

import com.ancore.ancoregaming.checkout.model.StockReservation;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IStockReservationRepository extends JpaRepository<StockReservation, UUID> {

  @Query("SELECT r FROM StockReservation r WHERE r.expirationTime < :now")
  List<StockReservation> findExpiredReservations(Instant now);
}
