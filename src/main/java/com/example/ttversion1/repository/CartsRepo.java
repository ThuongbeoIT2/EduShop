package com.example.ttversion1.repository;

import com.example.ttversion1.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartsRepo extends JpaRepository<Carts,Integer> {
    @Query("select o from carts o where o.user.email=:email")
    Optional<Carts> findCartsByUser(@Param("email") String email);
}
