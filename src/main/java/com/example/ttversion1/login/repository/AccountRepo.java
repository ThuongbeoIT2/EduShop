package com.example.ttversion1.login.repository;

import com.example.ttversion1.login.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepo extends JpaRepository<Account,Integer> {
    @Query("select o from account o where o.username=:username")
    Optional<Account> findByUsername(@Param("username") String username);
    @Query("select o from account o where o.status=:status order by o.accountID asc ")
    List<Account> getAccountByStatus(@Param("status") int status);
    @Query("select o from account o where o.roles.size=2 order by o.accountID asc ")
    List<Account> getAllByRoleAdmin();
    @Query("select o from account o where o.roles.size=1 order by o.accountID asc ")
    List<Account> getAllByRoleUser();
    @Query("select o from account o where o.user.email=:email")
    Optional<Account> findAccountByEmail( String email);
}
