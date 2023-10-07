package com.example.ttversion1.login.serviceImpl;


import com.example.ttversion1.login.entity.Decentralization;
import com.example.ttversion1.login.repository.DecentralizationRepo;
import com.example.ttversion1.login.service.IDecentralizationService;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DecentralizationService implements IDecentralizationService {

    @Autowired
    private DecentralizationRepo decentralizationRepo;
    @Override
    public List<Decentralization> GetAll() {
        return decentralizationRepo.findAll();
    }

    @Override
    public Optional<Decentralization> GetByName(String name) {
        Optional<Decentralization> RS = decentralizationRepo.findByName(name.trim());
        return RS;
    }

    @Override
    public void Save(Decentralization Obj) {
        decentralizationRepo.save(Obj);
    }

    @Override
    public void Insert(Decentralization decentralization) {
            Optional<Decentralization> RS = decentralizationRepo.findByName(decentralization.getAuthorityname().trim());
            if (RS.isEmpty()){
                Decentralization newObj= new Decentralization();
                newObj.setAuthorityname(decentralization.getAuthorityname().trim());
                newObj.setCreatedAt(Constants.getCurrentDay());
                newObj.setUpdateAt(Constants.getCurrentDay());
                decentralizationRepo.save(newObj);
            }
    }



    @Override
    public void Delete(String name) {
        Optional<Decentralization> RS = decentralizationRepo.findByName(name.trim());
        if (RS.isPresent()){
            decentralizationRepo.delete(RS.get());
            // Xoa tat ca cac account co role tren
        }
    }
}
