package com.example.ttversion1.NewsAndEvents.controller;


import com.example.ttversion1.NewsAndEvents.repository.EventsRepo;
import com.example.ttversion1.NewsAndEvents.repository.NewsRepo;
import com.example.ttversion1.NewsAndEvents.repository.PostDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    @Autowired
    private PostDetailRepo postDetailRepo;
    @Autowired
    private NewsRepo newsRepo;
    @Autowired
    private EventsRepo eventsRepo;

}
