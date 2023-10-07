package com.example.ttversion1.login.service;



import com.example.ttversion1.login.dto.UserDTO;
import com.example.ttversion1.login.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserDTO> GetAll();

    Optional<UserDTO> findUserByEmail(@Param("email") String email);
    void Save(User Obj);
    void Insert(UserDTO userDTO);
    void Update(UserDTO userDTO);
    void DeleteByEmail(@Param("email") String email);
}
