package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.Payment;
import com.example.ttversion1.repository.PaymentRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class PaymentController {
    @Autowired
    private PaymentRepo paymentRepo;
    @GetMapping(value = "/payment")
    List<Payment> GetAll(){
        return paymentRepo.findAll();
    }
    @PostMapping(value = "/admin/payment/insert")
    ResponseEntity<ResponseObject> insert(@RequestParam String method){
        Optional<Payment> payment= paymentRepo.GetByMethod(method.trim());
        if (payment.isEmpty()){
            Payment newObj = new Payment();
            newObj.setPaymentmethod(method.trim());
            newObj.setCreatedAt(Constants.getCurrentDay());
            newObj.setUpdatedAt(Constants.getCurrentDay());
            newObj.setStatus(0);
            paymentRepo.save(newObj);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.INSERT_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.ALREADY_EXIST,"")
            );
        }
    }
    @GetMapping(value = "/admin/payment/disable/{id}")
    ResponseEntity<ResponseObject> DisableMorthod(@PathVariable int id){
        Optional<Payment> payment = paymentRepo.findById(id);
        if (payment.isPresent()) {
            payment.get().setStatus(0);
            payment.get().setUpdatedAt(Constants.getCurrentDay());
            paymentRepo.save(payment.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.UPDATE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.NOT_FOUND, "")
            );
        }
    }
    @GetMapping(value = "/admin/payment/activate/{id}")
    ResponseEntity<ResponseObject> ActivateMethod(@PathVariable int id){
        Optional<Payment> payment = paymentRepo.findById(id);
        if (payment.isPresent()) {
            payment.get().setStatus(1);
            payment.get().setUpdatedAt(Constants.getCurrentDay());
            paymentRepo.save(payment.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.UPDATE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.NOT_FOUND, "")
            );
        }
    }
    @DeleteMapping(value = "/admin/payment/{method}")
    ResponseEntity<ResponseObject> delete(@PathVariable String method) {
        Optional<Payment> payment = paymentRepo.GetByMethod(method.trim());
        if (payment.isPresent()) {
            paymentRepo.delete(payment.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.DELETE_SUCCESS, "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("OK", Constants.NOT_FOUND, "")
            );
        }
    }

}
