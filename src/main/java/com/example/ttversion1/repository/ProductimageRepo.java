package com.example.ttversion1.repository;

import com.example.ttversion1.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductimageRepo extends JpaRepository<ProductImage,Integer> {
    @Query("select o from productimage o where o.status=:status order by o.productimageID asc ")
    List<ProductImage> GetAllByStatus(@Param("status") int status);
}
