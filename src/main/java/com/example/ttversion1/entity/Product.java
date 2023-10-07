package com.example.ttversion1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;


@Entity(name = "product")
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productID;
    @Basic
    @Column(nullable = false,unique = true)
    private String productname;
    @Basic
    @Column(nullable = false)
    private double price;
    @Basic
    @Column(nullable = false)
    private String avatarproduct;
    @Basic
    @Column(nullable = false)
    private String title;
    @Basic
    @Column
    private int discount=0;
    @Basic
    @Column(nullable = false)
    @Min(value = 0,message = "Số lượng sản phẩm phải lớn hơn 0")
    private int quantity=0;
    @Basic
    @Column(nullable = false)
    private int status=0;
    @Basic
    @Column(nullable = false)
    private int view=0;
    @Basic
    @Column(nullable = false)
    private Date createdAt;
    @Basic
    @Column(nullable = false)
    private Date updatedAt;
    @Basic
    @Column(nullable = false)
    @Min(value = 0,message = "Yêu cầu có ảnh")
    private int countImgDetaill=0;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductImage> productimages;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductReview> productreviews;
    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<CartItem> cartitems;
    @OneToMany(mappedBy = "product")

    @JsonManagedReference
    private List<OrderDetail> orderdetails;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producttypeID",foreignKey = @ForeignKey(name = "fk_product_type"))
    @JsonBackReference
    private ProductType producttype;
   @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ImportDetail> importDetails;
}
