package com.example.ttversion1.repository;

import com.example.ttversion1.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderdetailRepo extends JpaRepository<OrderDetail,Integer> {
    @Query("select o from orderdetail o where o.order.user.email=:email and o.product.productname=:productname and o.payment.paymentmethod=:method")
    Optional<OrderDetail> GetDetailByUserProduct(@Param("email") String email,@Param("productname") String productname,String method);
    @Query("select o from orderdetail o where o.order.user.email=: email and (o.orderstatus.orderstatusID=1 or o.orderstatus.orderstatusID=2) order by o.orderdetailID asc ")
    List<OrderDetail> GetDetailByUser(@Param("email") String email);
    @Query("select o from orderdetail o where o.order.user.email=: email and o.orderstatus.orderstatusID=1  order by o.orderdetailID asc ")
    List<OrderDetail> GetPayDetailByUser(@Param("email") String email);
    @Query("select o from orderdetail o where o.product.productname=:productname  order by o.orderdetailID asc ")
    List<OrderDetail> GetDetailByProduct(@Param("productname") String productname);
    @Query("select o from orderdetail o where o.payment.paymentmethod=:method  order by o.orderdetailID asc ")
    List<OrderDetail> GetDetailByPayMentMethod(@Param("method") String method);
    @Query("select o from orderdetail o where o.orderstatus.orderstatusID=:status  order by o.orderdetailID asc ")
    List<OrderDetail> GetDetailByOrderDetail(@Param("status") int status);
    @Query("select o from orderdetail  o where o.order.user.email=:email and o.orderdetailID=:odID")
    Optional<OrderDetail> GetDetailByUserOdID(String email,int odID);
    @Query("select o from orderdetail  o where o.order.user.email=:email and o.product.productname=:productname")
    Optional<OrderDetail> GetDetailByUserProduct(String email,String productname);
}
