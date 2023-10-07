package com.example.ttversion1.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity(name = "orderstatus")
@Table(name = "orderstatus")
@Getter
@Setter
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderstatusID;
    @Basic
    @Size(min = 6,max = 25,message = "tên từ 6-25 ký tự")
    @Column(nullable = false)
    private String statusname;
    @OneToMany(mappedBy = "orderstatus")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

}
