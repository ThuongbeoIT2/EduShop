package com.example.ttversion1.entity;

import com.example.ttversion1.NewsAndEvents.model.Voucher;
import com.example.ttversion1.login.entity.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "pay")
@Table(name = "pay")
@Data
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payID;
    @Column(nullable = false)
    private double originalprice= 0;
    @OneToOne
    @JoinColumn(name = "voucherID",foreignKey = @ForeignKey(name = "fk_pay_voucher"))
    private Voucher voucher;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID",foreignKey = @ForeignKey(name = "fk_account_pay"))
    @JsonBackReference
    private Account account;
    @OneToMany(mappedBy = "pay")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;
}
