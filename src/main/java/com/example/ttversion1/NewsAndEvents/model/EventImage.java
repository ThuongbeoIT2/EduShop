package com.example.ttversion1.NewsAndEvents.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "eventimage")
@Table(name = "eventimage")
@Data
public class EventImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventpostID;
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
    @JoinColumn(name = "eventID",foreignKey = @ForeignKey(name = "fk_event_post"))
    @JsonBackReference
    private Events events;

}
