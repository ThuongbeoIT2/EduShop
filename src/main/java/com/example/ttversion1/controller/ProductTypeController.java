package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.ProductType;
import com.example.ttversion1.repository.ProductTypeRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductTypeController {
    @Autowired
    private ProductTypeRepo productTypeRepo;
    @GetMapping("/productType")
    List<ProductType> GetAll(){
        return productTypeRepo.findAll();
    }
    @PostMapping(value = "/admin/producttype/insert")
    ResponseEntity<ResponseObject> insert( @RequestParam String name, @RequestParam MultipartFile newImgProType){
        Optional<ProductType> productType= productTypeRepo.findByName(name);
        if (productType.isEmpty()){
            if (newImgProType.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","File ảnh không được trống.","")
                );
            }else {

               ProductType newObj= new ProductType();
                newObj.setProducttypename(name.trim().toLowerCase());
                newObj.setCreatedAt(Constants.getCurrentDay());
                newObj.setUpdatedAt(Constants.getCurrentDay());
                Path path = Paths.get("uploadproducttypeimg/");
                try{
                    InputStream inputStream= newImgProType.getInputStream();
                    Files.copy(inputStream,path.resolve(name.trim().toLowerCase()+newImgProType.getOriginalFilename()),
                            StandardCopyOption.REPLACE_EXISTING);
                    newObj.setProducttypeimg(name.trim().toLowerCase()+newImgProType.getOriginalFilename().toLowerCase());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                productTypeRepo.save(newObj);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.INSERT_SUCCESS,newObj)
                );
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.ALREADY_EXIST,"")
            );
        }
    }

    @PutMapping(value = "/admin/producttype/update")
    ResponseEntity<ResponseObject> update(@RequestParam String typename,
                                          @RequestParam MultipartFile newImgProType){
        Optional<ProductType> productType = productTypeRepo.findByName(typename);

        if (productType.isPresent()){
            if (newImgProType.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","File ảnh không được trống.","")
                );
            }else {
                productType.get().setUpdatedAt(Constants.getCurrentDay());
                Path path = Paths.get("uploadproducttypeimg/");
                try{
                    Path oldImagePath = path.resolve(productType.get().getProducttypeimg());
                    if (Files.exists(oldImagePath)) {
                        Files.delete(oldImagePath); // Xóa tệp hình ảnh cũ
                    }
                    InputStream inputStream= newImgProType.getInputStream();
                    Files.copy(inputStream,path.resolve(typename.trim().toLowerCase()+newImgProType.getOriginalFilename().toLowerCase()),
                            StandardCopyOption.REPLACE_EXISTING);
                    productType.get().setProducttypeimg(typename.trim().toLowerCase()+newImgProType.getOriginalFilename().toLowerCase());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                productTypeRepo.save(productType.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.UPDATE_SUCCESS,"")
                );
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping(value = "/admin/productType/delete/{id}")
    ResponseEntity<ResponseObject> DeleteByID(@PathVariable int id){
        Optional<ProductType> productType = productTypeRepo.findById(id);
        if (productType.isPresent()){
            productTypeRepo.delete(productType.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }
}
