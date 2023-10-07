package com.example.ttversion1.repository;

import com.example.ttversion1.entity.ImportDetail;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.entity.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImportDetailRepo extends JpaRepository<ImportDetail,Integer> {
    @Query("select o.supplier from importdt o where o.product.productname=:productname")
    Optional<Suppliers> findSupplierByProduct(String productname);
    @Query("select o.product from importdt o where o.supplier.supplierID=:ID group by o.product order by o.product.productID asc ")
    List<Product> getProductBySupplierID(int ID);
    @Query("select o from importdt o where o.product.productname=:productname")
    List<ImportDetail> findImportByProduct(String productname);
    @Query("select o.product from importdt o where o.supplier.supplierID=:ID order by  o.createdAt desc ")
    List<ImportDetail> getImportBySupplierID(int ID);
}
