package com.ancore.ancoregaming.payment.repositories;

import com.ancore.ancoregaming.payment.model.Payment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentRepository extends JpaRepository<Payment, UUID> {

}
