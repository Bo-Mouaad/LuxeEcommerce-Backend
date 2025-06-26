package MainApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.Entities.Payment;

@Repository

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
