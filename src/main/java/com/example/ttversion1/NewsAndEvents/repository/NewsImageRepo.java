package com.example.ttversion1.NewsAndEvents.repository;

import com.example.ttversion1.NewsAndEvents.model.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsImageRepo extends JpaRepository<NewsImage,Integer> {
    @Query("select o from newsimage  o where  o.news.newID=:newID")
    List<NewsImage> GetImgByNewID(int newID);
}
