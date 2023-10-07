package com.example.ttversion1.repository;

import com.example.ttversion1.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Integer> {
    @Query("select o from cartitem o where o.product.productname=:productname and  o.carts.user.email=:email")
    Optional<CartItem> GetCartItemByProductnameEmail(@Param("productname") String productname, @Param("email") String email);
    @Query("select o from cartitem o where o.carts.user.email=:email order by o.cartitemID asc " )
    List<CartItem> GetItemByUser(@Param("email") String email);
}
