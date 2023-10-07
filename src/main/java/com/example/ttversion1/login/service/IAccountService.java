package com.example.ttversion1.login.service;

import com.example.ttversion1.login.dto.AccountDTO;
import com.example.ttversion1.login.entity.Account;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<AccountDTO> GetAllAdmin();
    List<AccountDTO> GetAllUser();
    Optional<AccountDTO> GetByUsername(@Param("name") String name);
    Optional<AccountDTO> findAccountByEmail(@Param("email") String email);
    void Save(Account Obj);
    void Insert(AccountDTO accountDTO);
    void Update(AccountDTO accountDTO);
    void Delete(@Param("username") String username);
}
