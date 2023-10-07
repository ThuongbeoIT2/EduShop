package com.example.ttversion1.NewsAndEvents.repository;

import com.example.ttversion1.NewsAndEvents.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepo extends JpaRepository<News,Integer> {
    @Query("select o from news o where o.title=:title")
    Optional<News> getByTitle(String title);
}
