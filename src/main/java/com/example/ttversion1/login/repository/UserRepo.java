package com.example.ttversion1.login.repository;

import com.example.ttversion1.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    @Query("select o from user o where o.email=:email")
    Optional<User> findByEmail(@Param("email") String email);
    @Query("select o from user o where o.username=:username")
    Optional<User> findByUsername(@Param("username") String username);

}
