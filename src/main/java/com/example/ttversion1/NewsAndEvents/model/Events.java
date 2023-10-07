package com.example.ttversion1.NewsAndEvents.model;

import com.example.ttversion1.entity.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "events")
@Table(name = "events")
@Data
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventID;
    @Column(nullable = false)
    private String eventtitle;
    @Column(nullable = false)
    private String avatarevent;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    @Column(nullable = false)
    private int countImg=0;
    @Column(nullable = false)
    private int discount;
    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date updateAt;
    @Column(nullable = false)
    private boolean hotflag;
    @OneToOne
    @JoinColumn(name = "productID",foreignKey = @ForeignKey(name = "fk_event_product"))
    private Product product;
    @OnDelete(action = OnDeleteAction.CASCADE)
   @OneToMany(mappedBy = "events")
    @JsonManagedReference
    private List<EventImage> eventImages;
}
