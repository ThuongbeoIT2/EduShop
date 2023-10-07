package com.example.ttversion1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity(name = "importdt")
@Table(name = "importdt")
@Data
public class ImportDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int ID;
    @Column(nullable = false)
    @Min(value = 1)
    private int quantity;
    @Column(nullable = false)
    private Date createdAt;
    @Basic
    @Column(nullable = false)
    private Date updatedAt;
    @Basic
    @Column(nullable = false)
    private double pricetotal;
    @Basic
    @Column(nullable = false)
    private double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierID",foreignKey = @ForeignKey(name = "fk_supplier_import"))
    @JsonBackReference
    private Suppliers supplier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productID",foreignKey = @ForeignKey(name = "fk_product_import"))
    @JsonBackReference
    private Product product;
}
