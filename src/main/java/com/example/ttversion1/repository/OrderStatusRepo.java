package com.example.ttversion1.repository;


import com.example.ttversion1.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrderStatusRepo extends JpaRepository<OrderStatus,Integer> {
    @Query("select o from orderstatus o where o.statusname=:name")
    Optional<OrderStatus> findByName(@Param("name") String name);
}
