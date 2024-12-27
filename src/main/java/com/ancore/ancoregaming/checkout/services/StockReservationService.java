package com.ancore.ancoregaming.checkout.services;

import com.ancore.ancoregaming.checkout.model.StockReservation;
import com.ancore.ancoregaming.checkout.repositories.IStockReservationRepository;
import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.repositories.IProductRepository;
import com.ancore.ancoregaming.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StockReservationService {
  private final IStockReservationRepository stockReservationRepository;
  private final IProductRepository productRepository;
  
  @Autowired
  public StockReservationService(IStockReservationRepository stockReservationRepository, IProductRepository productRepository) {
    this.stockReservationRepository = stockReservationRepository;
    this.productRepository = productRepository;
  }
  
  @Scheduled(cron = "0 */1 * * * *")
  @Transactional
  public void releaseExpiredReservations() {
    Instant now = Instant.now();
    List<StockReservation> expiredReservations = stockReservationRepository.findExpiredReservations(now);
    for (StockReservation reservation : expiredReservations) {
      Product product = reservation.getProduct();
      product.setStock(product.getStock() + reservation.getReservedQuantity());
      stockReservationRepository.deleteById(reservation.getId());
    }
  }

  public void createReservation(int quantity, User user, Product product) {
    StockReservation stockReservation = new StockReservation(quantity, user, product, Instant.now().plus(5, ChronoUnit.MINUTES));
    product.setStock(product.getStock() - quantity);
    productRepository.save(product);

    this.stockReservationRepository.save(stockReservation);
  }

  public void confirmPayment(UUID reservationId) {
    StockReservation reservation = stockReservationRepository.findById(reservationId)
            .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

    stockReservationRepository.delete(reservation);
  }
}
