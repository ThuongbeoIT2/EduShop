package com.example.ttversion1.NewsAndEvents.repository;

import com.example.ttversion1.NewsAndEvents.model.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventImageRepo extends JpaRepository<EventImage,Integer> {
    @Query("select o from eventimage o where o.events.eventID=:eventID order by o.eventpostID asc ")
    List<EventImage> GetImageByEventID(int eventID);

}
