package com.example.ttversion1.repository;

import com.example.ttversion1.entity.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepo extends JpaRepository<Suppliers,Integer> {
    @Query("select o from supplier o where  o.phone=:phone")
Optional<Suppliers> GetByPhone(String phone);
}
