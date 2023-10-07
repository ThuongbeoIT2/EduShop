package com.example.ttversion1.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.dto.Item;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.repository.CartItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CartsController {
    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @GetMapping(value = "/carts")
    ResponseEntity<ResponseObject> GetItemByUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        String email = account.getUser().getEmail();
        List<Item> items= cartItemRepo.GetItemByUser(email.trim()).stream().map(
               cartItem -> {
                   Item item = new Item();
                   item.setQuantity(cartItem.getQuantity());
                   item.setProductname(cartItem.getProduct().getProductname());
                   item.setCreatedAt(cartItem.getCreatedAt());
                   return item;
               }
        ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Giỏ hàng của User có mail "+ email,items)
        );
    }

}
