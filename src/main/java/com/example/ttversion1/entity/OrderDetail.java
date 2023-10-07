package com.example.ttversion1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity(name = "orderdetail")
@Table(name = "orderdetail")
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderdetailID;
    @Basic
    @Column(nullable = false)
    private double pricetotal;
    @Basic
    @Column(nullable = false)
    @Min(value = 0,message = "Số lượng mua phải lớn hơn 0")
    private int quantity;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @Basic
    @Column(nullable = false)
    private Date updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentID",foreignKey = @ForeignKey(name = "fk_payment_orderdetail"))
    @JsonBackReference
    private Payment payment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderstatusID",foreignKey = @ForeignKey(name = "fk_status_orderdetail"))
    @JsonBackReference
    private OrderStatus orderstatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderID",foreignKey = @ForeignKey(name = "fk_order_detail"))
    @JsonBackReference
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productID",foreignKey = @ForeignKey(name = "fk_product_orderdetail"))
    @JsonBackReference
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payID",foreignKey = @ForeignKey(name = "fk_pay_orderdetail"))
    @JsonBackReference
    private Pay pay;
}
