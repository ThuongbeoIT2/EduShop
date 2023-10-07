package com.example.ttversion1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "productimage")
@Entity(name = "productimage")
@Data
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productimageID;
    @Basic
    @Column
    private String title;
    @Basic
    @Column(nullable = false)
    private String imageproduct;
    @Basic
    @Column(nullable = false)
    private int status;
    @Basic
    @Column(nullable = false)
    private Date createdAt;

    @Basic
    @Column(nullable = false)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productID",foreignKey = @ForeignKey(name = "fk_product_img"))
    @JsonBackReference
    private Product product;
}
