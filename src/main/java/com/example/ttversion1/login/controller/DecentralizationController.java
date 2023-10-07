package com.example.ttversion1.login.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.login.entity.Decentralization;
import com.example.ttversion1.login.service.IDecentralizationService;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DecentralizationController {
    @Autowired
    private IDecentralizationService decentralizationService;
    @GetMapping(value = "/Decentralization/getAll")
    List<Decentralization> getAll(){

        return decentralizationService.GetAll(); // chua phan trang
    }
    @GetMapping(value = "/Decentralization/search")
    ResponseEntity<ResponseObject> GetDecentralizationByName(@RequestParam("name") String name){
        Optional<Decentralization> RS = decentralizationService.GetByName(name.trim());
        if (RS.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.OK,RS.get())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_IMPLEMENT,"")
            );
        }
    }
    @PostMapping(value = "/Decentralization/insert")
    ResponseEntity<ResponseObject> insert(@RequestParam String name){
        Optional<Decentralization> RS = decentralizationService.GetByName(name.trim());
        if(RS.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.ALREADY_EXIST,"")
            );
        }else {
            Decentralization newBoj= new Decentralization();
            newBoj.setAuthorityname(name.trim());
            decentralizationService.Insert(newBoj);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.INSERT_SUCCESS,"")
            );

        }
    }
    @DeleteMapping(value = "/Decentralization/delete")
    ResponseEntity<ResponseObject> delete(@RequestParam String name){
        Optional<Decentralization> RS = decentralizationService.GetByName(name.trim());
        if (RS.isPresent()) {
            decentralizationService.Delete(name.trim());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND",Constants.NOT_FOUND,"")
            );
        }
    }

}
