package com.example.ttversion1.NewsAndEvents.model;

import com.example.ttversion1.login.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity(name = "news")
@Table(name = "news")
@Data
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int newID;
    @Column(nullable = false,unique = true)
    @Size(min = 5,max = 30)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String avatarpost;
    private int countImg=0;
    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date updateAt;
    @Column(nullable = false)
    private boolean hotflag;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID",foreignKey = @ForeignKey(name = "fk_user_new"))
    @JsonBackReference
    private User user;
    @OneToMany(mappedBy = "news")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<NewsImage> newsImages;
}
