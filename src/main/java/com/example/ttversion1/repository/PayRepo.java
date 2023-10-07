package com.example.ttversion1.repository;

import com.example.ttversion1.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayRepo extends JpaRepository<Pay,Integer> {
    @Query("select o from pay o where o.account.user.email=:email")
    List<Pay> PayLogByUser(String email);
}
