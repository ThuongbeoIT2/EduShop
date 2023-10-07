package com.example.ttversion1.login.repository;



import com.example.ttversion1.login.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
   @Query("select o from verification o where o.token=:token")
    VerificationToken findByToken(String token);
    @Query("select o from verification o where o.account.username=:username")
    VerificationToken findByAccount(@Param("username") String username);
}
