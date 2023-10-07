package com.example.ttversion1.NewsAndEvents.model;


import com.example.ttversion1.login.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "voucher")
@Table(name = "voucher")
@Data
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int voucherID;
    @Column(nullable = false,unique = true)
    private String code;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private int discount;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID",foreignKey = @ForeignKey(name = "fk_user_voucher"))
    @JsonBackReference
    private User user;


}
