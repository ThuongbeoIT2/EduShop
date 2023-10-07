package com.example.ttversion1.repository;

import com.example.ttversion1.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPreviewRepo extends JpaRepository<ProductReview,Integer> {
    @Query("select o from productreview o where o.user.email=:email order by o.productreviewID desc ")
    List<ProductReview> GetReviewByUser(String email);
    @Query("select o from productreview o where  o.product.productname=:productname and o.status=1 order by o.productreviewID desc ")
    List<ProductReview> GetReviewByProduct(String productname);
    @Query("select o from productreview o where o.user.email=:email and o.product.productname=:productname order by o.productreviewID desc ")
    List<ProductReview> GetByUserProduct(String email,String productname);
}
