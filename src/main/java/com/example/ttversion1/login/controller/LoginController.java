package com.example.ttversion1.login.controller;


import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class LoginController {
    @Autowired
    private AccountRepo accountRepo;
    @PostMapping(value = "/login")
    ResponseEntity<ResponseObject> Login(@RequestParam(value = "username",required = true) String username,
                                         @RequestParam(value = "password",required = true) String password,
                                         HttpSession session){
        System.out.println(username+"  "+ password);
        Optional<Account> account= accountRepo.findByUsername(username.trim());
        System.out.println(account);
        if (account.isPresent()){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password.trim());
            if (passwordEncoder.matches(account.get().getPassword(),encodedPassword)){
                if (account.get().isEnabled()){
                    account.get().setStatus(1);// Bật onl
                    accountRepo.save(account.get());
                    System.out.println(account.get());
                    session.setAttribute("username",username);
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("OK",Constants.LOGIN_SUCCESS,"")
                    );

                }else {
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("FAILED","Tài khoản của bạn chưa được kích hoạt","")
                    );
                }
            }else {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED",Constants.LOGIN_FAIL_PASSWORD,"")
                );
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", Constants.ACCOUNT_NOT_EXIST,"")
            );
        }
    }
    @GetMapping(value = "/logout")
    ResponseEntity<ResponseObject> Logout(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
        account.setStatus(0);// Bật offline
        accountRepo.save(account);
        System.out.println(account.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Đăng xuất thành công","")
        );
    }
    @GetMapping(value = "/login")
    ResponseEntity<ResponseObject> HomeLogin(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Xin chào hehe ","")
        );
    }
    @GetMapping("/home")
    ResponseEntity<ResponseObject> Home(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepo.findByUsername(auth.getName()).get();
       if (account.getRoles().size()==2){
           account.setStatus(1);// Bật onl
           accountRepo.save(account);
           System.out.println(account);
           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("OK","Trang Home Admin","")
           );
       }else {
           account.setStatus(1);// Bật onl
           accountRepo.save(account);
           System.out.println(account);
           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("OK","Trang Home User","")
           );
       }
    }

}
