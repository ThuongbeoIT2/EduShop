package com.example.ttversion1.login.service;


import com.example.ttversion1.login.entity.Decentralization;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IDecentralizationService {
    List<Decentralization> GetAll();
    Optional<Decentralization> GetByName(@Param("name") String name);
    void Save(Decentralization Obj);
    void Insert(Decentralization decentralizationDTO);

    void Delete(@Param("name") String name);

}
