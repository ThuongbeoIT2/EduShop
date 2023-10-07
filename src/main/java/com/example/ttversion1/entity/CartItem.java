package com.example.ttversion1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity(name = "cartitem")
@Table(name = "cartitem")
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartitemID;
    @Basic
    @Min(value = 0,message = "Số lượng phải không âm")
    @Column(nullable = false)
    private int quantity;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @Basic
    @Column(nullable = false)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productID",foreignKey = @ForeignKey(name = "fk_cartitem_product"))
    @JsonBackReference
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartID",foreignKey = @ForeignKey(name = "fk_cartitem_cart"))
    @JsonBackReference
    private Carts carts;
}
