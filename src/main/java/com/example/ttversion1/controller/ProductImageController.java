package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.entity.ProductImage;
import com.example.ttversion1.repository.ProductRepo;
import com.example.ttversion1.repository.ProductimageRepo;
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
public class ProductImageController {
    @Autowired
    private ProductimageRepo productimageRepo;
    @Autowired
    private ProductRepo productRepo;
    @GetMapping("/productImg")
    List<ProductImage> GetAll(){
        return productimageRepo.findAll();
    }
    @GetMapping(value = "/admin/productImg/{status}")
    ResponseEntity<ResponseObject> GetByStatus(@PathVariable int status){

         return ResponseEntity.status(HttpStatus.OK).body(
                 new ResponseObject("OK","Lấy thành công các ProductImage có status :"+ status, productimageRepo.GetAllByStatus(status))
         );
    }
//    @PutMapping(value = "/productImg/update")
//    ResponseEntity<ResponseObject> update(@RequestParam int id, @RequestParam String title, @RequestParam MultipartFile newimgProduct){
//        Optional<ProductImage> productImage = productimageRepo.findById(id);
//        if (productImage.isPresent()){
//           productImage.get().setTitle(title);
//           productImage.get().setUpdatedAt(Constants.getCurrentDay());
//            Path path = Paths.get("uploadphotoproducts/");
//            try{
//                Path oldImagePath = path.resolve(productImage.get().getImageproduct());
//                if (Files.exists(oldImagePath)) {
//                    Files.delete(oldImagePath); // Xóa tệp hình ảnh cũ
//                }
//                InputStream inputStream= newimgProduct.getInputStream();
//                Files.copy(inputStream,path.resolve(productImage.get().getProduct().getProductname().trim().toLowerCase()+newimgProduct.getOriginalFilename()),
//                        StandardCopyOption.REPLACE_EXISTING);
//                productImage.get().setImageproduct(productImage.get().getProduct().getProductname().trim().toLowerCase()+newimgProduct.getOriginalFilename().toLowerCase());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//           productimageRepo.save(productImage.get());
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("OK", Constants.UPDATE_SUCCESS,"")
//            );
//        }else {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
//            );
//        }
//    }
    @GetMapping(value = "/productImg/delete/{id}")
    ResponseEntity<ResponseObject> DeleteByID(@PathVariable int id){
        Optional<ProductImage> productImage = productimageRepo.findById(id);
        if (productImage.isPresent()){
            productimageRepo.delete(productImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.DELETE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping(value = "/product/{productname}/productimage")
    ResponseEntity<ResponseObject> GetProductImgByProductName(@PathVariable String productname){
        Optional<Product> product = productRepo.findProductByName(productname);
        if (product.isPresent()){
            List<ProductImage> productImages= product.get().getProductimages();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.OK,productImages)
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping(value = "/admin/productimg/status-1/{id}")
    ResponseEntity<ResponseObject> ChangeStatus1(@PathVariable int id){
        Optional<ProductImage> productImage = productimageRepo.findById(id);
        if (productImage.isPresent()){
            productImage.get().setStatus(1);
            productimageRepo.save(productImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cập nhật hiển thị thành công","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại ProductImg yêu cầu ", "")
            );
        }
    }
    @GetMapping(value = "/admin/productimg/status-0/{id}")
    ResponseEntity<ResponseObject> ChangeStatus0(@PathVariable int id){
        Optional<ProductImage> productImage = productimageRepo.findById(id);
        if (productImage.isPresent()){
            productImage.get().setStatus(0);
            productimageRepo.save(productImage.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Cập nhật tắt hiển thị","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND", "Không tồn tại ProductImage yêu cầu ", "")
            );
        }
    }
    @PostMapping(value = "/admin/product/insertimg")
    ResponseEntity<ResponseObject> insertProductImg(@RequestParam String productname,
                                                    @RequestParam String title,
                                                    @RequestParam MultipartFile productimg){
        Optional<Product> product = productRepo.findProductByName(productname);
        if (product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }else {
            if (productimg.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED",Constants.IMG_NOT_FOUND,"")
                );
            }else {
                ProductImage productImage= new ProductImage();
                productImage.setProduct(product.get());
                product.get().setCountImgDetaill(product.get().getCountImgDetaill()+1);
                productRepo.save(product.get());
                productImage.setStatus(0);
                productImage.setCreatedAt(Constants.getCurrentDay());
                productImage.setUpdatedAt(Constants.getCurrentDay());
                productImage.setTitle(title);
                Path path = Paths.get("uploadphotoproducts/");
                try{

                    InputStream inputStream= productimg.getInputStream();
                    Files.copy(inputStream,path.resolve(productname.trim().toLowerCase()+product.get().getCountImgDetaill()+productimg.getOriginalFilename()),
                            StandardCopyOption.REPLACE_EXISTING);
                    productImage.setImageproduct(productname.trim().toLowerCase()+product.get().getCountImgDetaill()+productimg.getOriginalFilename().toLowerCase());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                productimageRepo.save(productImage);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", Constants.INSERT_SUCCESS,"")
                );
            }
        }
    }
}
