package com.example.ttversion1.repository;

import com.example.ttversion1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query("select o from product o where o.productname=:productname")
    Optional<Product> findProductByName(@Param("productname")String productname);
    @Query("select o from product o where o.status=:status order by o.productID asc ")
    List<Product> GetProductByStatus(@Param("status") int status);
    @Query("select o from product o where o.producttype.producttypename=:typename order by o.productID asc ")
    List<Product> GetByProductType(@Param("typename") String typename);

}
