package com.example.ttversion1.repository;

import com.example.ttversion1.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Integer> {
    @Query("select o from payment  o where  o.paymentmethod=:method")
    Optional<Payment> GetByMethod(@Param("method") String method);
    @Query("select o from payment  o where o.status=:status order by o.paymentID asc ")
    List<Payment> GetPaymentStatus(int status);
}
