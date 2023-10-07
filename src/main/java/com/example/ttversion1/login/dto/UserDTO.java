package com.example.ttversion1.login.dto;

import com.example.ttversion1.login.rule.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    @Size(min = 6,max = 25,message = "Username từ 6-25 ký tự")
    private String username;
    @Size(min = 10,max = 10,message = "Số điện thoại có định dạng 0xxxxxxxxx")
    private String phone;
    @NotNull
   @ValidEmail
    private String email;
    @NotNull
    private String address;

}
