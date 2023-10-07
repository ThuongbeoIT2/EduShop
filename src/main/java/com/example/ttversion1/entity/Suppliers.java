package com.example.ttversion1.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "supplier")
@Table(name = "supplier")
@Data
public class Suppliers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int supplierID;
    @Column(unique = true,nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false,unique = true)
    private String phone;
    @OneToMany(mappedBy = "supplier")
    @JsonManagedReference
    private List<ImportDetail> importDetails;
}
