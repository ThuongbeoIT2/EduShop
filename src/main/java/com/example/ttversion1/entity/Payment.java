package com.example.ttversion1.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "payment")
@Table(name = "payment")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentID;
    @Basic
    @Column(nullable = false)
    private String paymentmethod;
    @Basic
    @Column(nullable = false)
    private int status;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @Basic
    @Column(nullable = false)
    private Date updatedAt;
    @OneToMany(mappedBy = "payment")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}
