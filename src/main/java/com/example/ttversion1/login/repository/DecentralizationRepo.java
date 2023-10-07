package com.example.ttversion1.login.repository;

import com.example.ttversion1.login.entity.Decentralization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DecentralizationRepo extends JpaRepository<Decentralization,Integer> {
    @Query("select o from decentralization o where o.authorityname=:name ")
    Optional<Decentralization> findByName(@Param("name") String name);
}
