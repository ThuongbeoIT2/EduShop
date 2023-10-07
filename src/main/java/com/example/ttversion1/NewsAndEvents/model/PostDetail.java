package com.example.ttversion1.NewsAndEvents.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity(name = "postdetail")
@Table(name = "postdetail")
@Data
public class PostDetail {
    // Cái này dùng khi muốn tạo bài viết kết hợp sự kiện
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PostID;
    @Column(nullable = false)
    private String title;
    @OneToOne
    @JoinColumn(name = "newsID",foreignKey = @ForeignKey(name = "nfk_news_postdetail"))
    private News news;
    @OneToOne
    @JoinColumn(name = "eventID",foreignKey = @ForeignKey(name = "nfk_event_postdetail"))
    private Events events;

    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date updateAt;
    @Column(nullable = false)
    private boolean hotflag;
}
