package com.example.ttversion1.login.controller;

import com.example.ttversion1.ResponseObject;
import com.example.ttversion1.entity.Carts;
import com.example.ttversion1.login.dto.UserDTO;
import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.entity.User;
import com.example.ttversion1.login.repository.AccountRepo;
import com.example.ttversion1.login.repository.UserRepo;
import com.example.ttversion1.login.service.IAccountService;
import com.example.ttversion1.login.service.IUserService;
import com.example.ttversion1.repository.CartsRepo;
import com.example.ttversion1.shareds.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartsRepo cartsRepo;

    @GetMapping(value = "/admin/searchuser/{email}")
    ResponseEntity<ResponseObject> getUserByEmail(@PathVariable String email){
        Optional<UserDTO> RS = userService.findUserByEmail(email.trim());
        if (RS.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", Constants.OK,RS.get())
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("NOT FOUND",Constants.NOT_FOUND,"")
            );
        }
    }
   @PostMapping(value = "/user/insert")
    ResponseEntity<ResponseObject> insert(@RequestParam String username,
                                          @RequestParam String phone,
                                          @RequestParam String email,
                                          @RequestParam String address){
        Optional<UserDTO> userDTO= userService.findUserByEmail(email.trim());
        if (userDTO.isEmpty()){
            UserDTO newUserDTO= new UserDTO();
            newUserDTO.setUsername(username);
            newUserDTO.setPhone(phone.trim());
            newUserDTO.setAddress(address);
            newUserDTO.setEmail(email.trim().toLowerCase());
            userService.Insert(newUserDTO);
            Carts newCarts= new Carts();
            User user = userRepo.findByEmail(email.trim()).get();
            newCarts.setUser(user);
            newCarts.setCreatedAt(Constants.getCurrentDay());
            newCarts.setUpdatedAt(Constants.getCurrentDay());
            cartsRepo.save(newCarts);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.INSERT_SUCCESS,"")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.ALREADY_EXIST,"")
            );
        }
   }
   @PutMapping(value = "/user/update")
   ResponseEntity<ResponseObject> update(@RequestParam String username,
                                         @RequestParam String phone,
                                         @RequestParam String email,
                                         @RequestParam String address){
       Optional<UserDTO> userDTO= userService.findUserByEmail(email.trim().toLowerCase());
       if(userDTO.isPresent()){
           userDTO.get().setUsername(username);
           userDTO.get().setPhone(phone.trim());
           userDTO.get().setAddress(address);
           userService.Update( userDTO.get());
           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("OK",Constants.UPDATE_SUCCESS,"")
           );
       }else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                   new ResponseObject("NOT FOUND",Constants.NOT_FOUND,"")
           );
       }
   }
   @DeleteMapping("/user/delete")
    ResponseEntity<ResponseObject> delete(@RequestParam String email){
        Optional<UserDTO> RS= userService.findUserByEmail(email.trim());
        if (RS.isPresent()){
            userService.DeleteByEmail(email);
            Account account = accountRepo.findAccountByEmail(email.trim()).get();
            accountService.Delete(account.getUsername());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",Constants.DELETE_SUCCESS,"")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED",Constants.NOT_IMPLEMENT,"")
            );
        }
   }
}
