package com.example.ttversion1.NewsAndEvents.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "newsimage")
@Entity(name = "newsimage")
@Data
public class NewsImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int newpostID;
    @Column(nullable = false,unique = true)
    private String title;
    @Column(nullable = false)
    private String postImg;
    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date updateAt;
    @Column(nullable = false)
    private boolean status=false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsID",foreignKey = @ForeignKey(name = "fk_news_post"))
    @JsonBackReference
    private News news;
}
