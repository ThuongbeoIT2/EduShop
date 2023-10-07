package com.example.ttversion1.entity;

import com.example.ttversion1.login.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity(name = "productreview")
@Table(name = "productreview")
@Data
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productreviewID;
    @Basic
    @Column
    private String contentrated;
    @Basic
    @Min(value = 1,message = "Thấp nhất là 1 điểm")
    @Max(value = 10,message = "Cao nhất là 10 điểm.Không có nhưng hehe")
    @Column(nullable = false)
    private int pointevaluation;
    @Basic
    @Column
    private String contentseen;
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
    @JoinColumn(name = "userID",foreignKey = @ForeignKey(name = "fk_user_proreview"))
    @JsonBackReference
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productID",foreignKey = @ForeignKey(name = "fk_product_proreview"))
    @JsonBackReference
    private Product product;
}
