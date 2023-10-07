package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.OrderDetail;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.entity.ProductReview;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.repository.OrderdetailRepo;
import com.example.ttversion1.repository.ProductPreviewRepo;
import com.example.ttversion1.repository.ProductRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductReviewController {
    @Autowired
    private ProductPreviewRepo productPreviewRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private OrderdetailRepo orderdetailRepo;
    @GetMapping(value = "/review/product/{productname}")
    ResponseEntity<ResponseObject>GetReviewByProduct(@PathVariable String productname){
        Optional<Product> product = productRepo.findProductByName(productname);
        List<ProductReview> productReviews= productPreviewRepo.GetReviewByProduct(productname);
        if (product.isPresent()){
           return  ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("OK",Constants.OK,productReviews)
           );
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("NOT FOUND", Constants.NOT_FOUND,"")
            );
        }
    }
    @PostMapping(value = "/product/productreview")
    ResponseEntity<ResponseObject> Insert(@RequestParam String productname,
                                          @RequestParam  int pointevaluation,
                                          @RequestParam String contentseen,
                                          @RequestParam String contentrated){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        Optional<OrderDetail> orderDetail = orderdetailRepo.GetDetailByUserProduct(account.getUser().getEmail(),productname);
        if (orderDetail.isPresent() && pointevaluation>=0 && pointevaluation<=10){
            ProductReview productReview = new ProductReview();
            productReview.setProduct(orderDetail.get().getProduct());
            productReview.setStatus(1);
            productReview.setContentrated(contentrated);
            productReview.setContentseen(contentseen);
            productReview.setPointevaluation(pointevaluation);
            productReview.setCreatedAt(Constants.getCurrentDay());
            productReview.setUpdatedAt(Constants.getCurrentDay());
            productReview.setUser(account.getUser());
            productPreviewRepo.save(productReview);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.INSERT_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND",Constants.NOT_FOUND,"")
            );
        }

    }
    @PutMapping(value = "/product/productreview/update/{idreview}")
    ResponseEntity<ResponseObject> Insert(@RequestParam  int pointevaluation,
                                          @RequestParam String contentseen,
                                          @RequestParam String contentrated,
                                          @PathVariable int idreview){
        Optional<ProductReview> productReview = productPreviewRepo.findById(idreview);
        if ( productReview.isPresent() && pointevaluation>=0 && pointevaluation<=10){
            productReview.get().setStatus(1);
            productReview.get().setContentrated(contentrated);
            productReview.get().setContentseen(contentseen);
            productReview.get().setPointevaluation(pointevaluation);
            productReview.get().setUpdatedAt(Constants.getCurrentDay());
            productPreviewRepo.save(productReview.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.UPDATE_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND",Constants.NOT_FOUND,"")
            );
        }
    }
    @GetMapping (value = "/product/productreview/disable/{idreview}")
    ResponseEntity<ResponseObject> Status(@PathVariable int idreview){
        Optional<ProductReview> productReview = productPreviewRepo.findById(idreview);
        if ( productReview.isPresent()){
           productReview.get().setStatus(0);
           productPreviewRepo.save(productReview.get());
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
