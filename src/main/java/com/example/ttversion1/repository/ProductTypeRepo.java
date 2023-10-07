package com.example.ttversion1.repository;

import com.example.ttversion1.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTypeRepo extends JpaRepository<ProductType,Integer> {
    @Query("select o from producttype o where o.producttypename=:name")
    Optional<ProductType> findByName(@Param("name") String name);
}
