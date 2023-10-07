package com.example.ttversion1.login.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "decentralization")
@Table(name = "decentralization")
@Getter
@Setter
public class Decentralization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int decentralizationID;
    @Basic
    @Column(nullable = false,unique = true)
    private String authorityname;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @Basic
    @Column(nullable = true)
    private Date updateAt;
//    @OneToMany(mappedBy = "decentralization")
//    @JsonManagedReference
//    private List<Account> accounts;
}
