package com.example.ttversion1.entity;

import com.example.ttversion1.login.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity(name = "ordertbl")
@Table(name = "ordertbl")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderID;
    @Basic
    @Column(nullable = false)
    private double originalprice= 0;
    @Basic
    @Column(nullable = false)
    private double actualprice= 0;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @Basic
    @Column(nullable = false)
    private Date updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID",foreignKey = @ForeignKey(name = "fk_user_ordertbl"))
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private List<OrderDetail> ordersDetail;
}
