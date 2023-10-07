package com.example.ttversion1.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "producttype")
@Table(name = "producttype")
@Data
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int producttypeID;
    @Basic
    @Column(nullable = false,unique = true)
    private String producttypename;
    @Basic
    @Column(nullable = false)
    private String producttypeimg;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @Basic
    @Column(nullable = false)
    private Date updatedAt;
    @OneToMany(mappedBy = "producttype")
    @JsonManagedReference
    List<Product> product;
}
