package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.OrderStatus;
import com.example.ttversion1.repository.OrderStatusRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class OrderStatusController {
    @Autowired
    private OrderStatusRepo orderStatusRepo;
    @GetMapping(value = "/admin/orderstatus")
    List<OrderStatus> GetAll(){
        return orderStatusRepo.findAll();

    }
    @PostMapping(value = "/admin/orderstatus/insert")
    ResponseEntity<ResponseObject> insert(@RequestParam String name){
        Optional<OrderStatus> orderStatus= orderStatusRepo.findByName(name.trim());
        if (orderStatus.isEmpty()){
            OrderStatus newObj = new OrderStatus();
            newObj.setStatusname(name.trim());
            orderStatusRepo.save(newObj);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.INSERT_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.ALREADY_EXIST,"")
            );
        }
    }
    @DeleteMapping(value = "/admin/orderstatus/{name}")
    ResponseEntity<ResponseObject> delete(@PathVariable String name){
        Optional<OrderStatus> orderStatus= orderStatusRepo.findByName(name.trim());
        if (orderStatus.isPresent()){
            orderStatusRepo.delete(orderStatus.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.NOT_FOUND,"")
            );
        }
    }
}
