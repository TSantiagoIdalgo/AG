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

  @Autowired
  private IStockReservationRepository stockReservationRepository;
  @Autowired
  private IProductRepository productRepository;

  @Scheduled(cron = "0 */1 * * * *") // Cada 5 minutos
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

  public StockReservation createReservation(int cuantity, User user, Product product) {
    StockReservation stockReservation = new StockReservation(cuantity, user, product, Instant.now().plus(5, ChronoUnit.MINUTES));
    product.setStock(product.getStock() - cuantity);
    productRepository.save(product);

    return this.stockReservationRepository.save(stockReservation);
  }

  public void confirmPayment(UUID reservationId) {
    StockReservation reservation = stockReservationRepository.findById(reservationId)
            .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

    stockReservationRepository.delete(reservation);
  }
}
