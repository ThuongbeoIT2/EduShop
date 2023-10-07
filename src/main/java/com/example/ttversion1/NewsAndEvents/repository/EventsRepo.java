package com.example.ttversion1.NewsAndEvents.repository;

import com.example.ttversion1.NewsAndEvents.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventsRepo extends JpaRepository<Events,Integer> {
    @Query("select o from events  o where o.eventtitle=:eventtitle and o.product.productname=:productname")
    Optional<Events> GetEvents(String eventtitle,String productname);
}
