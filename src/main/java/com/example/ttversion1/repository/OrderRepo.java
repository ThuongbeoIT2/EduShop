package com.example.ttversion1.repository;

import com.example.ttversion1.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    @Query("select o from ordertbl o where o.user.email=:email")
    Optional<Order> GetOrderTblByUser(@Param("email") String email);
}
