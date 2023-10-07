package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.entity.Suppliers;
import com.example.ttversion1.repository.ImportDetailRepo;
import com.example.ttversion1.repository.SupplierRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SupplierController {
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private ImportDetailRepo importDetailRepo;
    @GetMapping(value = "/supplier")
    List<Suppliers> GetAll(){
        return supplierRepo.findAll();
    }
    @GetMapping(value = "/supplier/product/{ID}")
    ResponseEntity<ResponseObject> ProductBySupplierID(@PathVariable int ID){
        Optional<Suppliers> suppliers= supplierRepo.findById(ID);
        if (suppliers.isPresent()){
            List<Product> products= importDetailRepo.getProductBySupplierID(ID);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.OK,products)
            );
        }else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", Constants.NOT_FOUND,"")
            );
        }
    }
    @PostMapping(value = "/admin/supplier/insert")
    ResponseEntity<ResponseObject> Insert(@RequestParam String name,
                                          @RequestParam String phone,
                                          @RequestParam String address){
        Optional<Suppliers> suppliers = supplierRepo.GetByPhone(phone.trim());
        if (suppliers.isEmpty()){
            Suppliers newObj = new Suppliers();
            newObj.setName(name);
            newObj.setPhone(phone.trim());
            newObj.setAddress(address);
            supplierRepo.save(newObj);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.INSERT_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.ALREADY_EXIST,"")
            );
        }
    }
    @PutMapping(value = "/admin/supplier/insert/{ID}")
    ResponseEntity<ResponseObject> Update(@RequestParam String name,
                                          @RequestParam String phone,
                                          @RequestParam String address,
                                          @PathVariable int ID){
        Optional<Suppliers> suppliers = supplierRepo.findById(ID);
        if (suppliers.isPresent()){
           suppliers.get().setName(name);
           suppliers.get().setAddress(address);
           suppliers.get().setPhone(phone);
           supplierRepo.save(suppliers.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.UPDATE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_IMPLEMENT,"")
            );
        }
    }
    @GetMapping(value = "/admin/supplier/delete/{ID}")
    ResponseEntity<ResponseObject> DELETE(
                                          @PathVariable int ID){
        Optional<Suppliers> suppliers = supplierRepo.findById(ID);
        if (suppliers.isPresent()){
            supplierRepo.delete(suppliers.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_IMPLEMENT,"")
            );
        }
    }
}
