package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.CartItem;
import com.example.ttversion1.entity.Carts;
import com.example.ttversion1.entity.Product;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.repository.CartItemRepo;
import com.example.ttversion1.repository.CartsRepo;
import com.example.ttversion1.repository.ProductRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class CartItemController {
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private CartsRepo cartsRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private AccountRepo accountRepo;

    @PostMapping(value = "/user/insertcartitem")
    ResponseEntity<ResponseObject> Insert(
                                          @RequestParam String productname,
                                          @RequestParam int quantity){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        Optional<CartItem> cartItem = cartItemRepo.GetCartItemByProductnameEmail(productname, email.trim());
        Optional<Carts> carts = cartsRepo.findCartsByUser(email.trim());
        Optional< Product> product = productRepo.findProductByName(productname);
        if (quantity<0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Số lượng mua là các số nguyên dương","")
            );
        }else
        if (cartItem.isPresent()){
            cartItem.get().setQuantity(cartItem.get().getQuantity()+ quantity);
            cartItemRepo.save(cartItem.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.UPDATE_SUCCESS,cartItem)
            );
        }else {
            if (carts.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED",Constants.USER_NOT_FOUND,"")
                );
            }else if (product.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED","Không tìm thấy Product yêu cầu","")
                );
            }else {
                CartItem newObj = new CartItem();
                newObj.setQuantity(quantity);
                newObj.setProduct(product.get());
                newObj.setCreatedAt(Constants.getCurrentDay());
                newObj.setUpdatedAt(Constants.getCurrentDay());
                newObj.setCarts(carts.get());
                cartItemRepo.save(newObj);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK",Constants.INSERT_SUCCESS,"")
                );
            }

        }
    }
    @PutMapping(value = "/user/updatecartitem")
    ResponseEntity<ResponseObject> Update(@RequestParam String productname,
                                          @RequestParam int quantity){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        Optional<CartItem> cartItem = cartItemRepo.GetCartItemByProductnameEmail(productname, email.trim());
        if (quantity<0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED","Số lượng mua là các số nguyên dương","")
            );
        }else if (cartItem.isPresent()){
            cartItem.get().setQuantity(quantity);
            cartItemRepo.save(cartItem.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.UPDATE_SUCCESS,cartItem)
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_FOUND,"")
            );
        }
    }


}
