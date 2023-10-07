package com.example.ttversion1.login.dto;

import com.example.ttversion1.login.entity.Decentralization;
import com.example.ttversion1.login.rule.PasswordMatches;
import com.example.ttversion1.login.rule.ValidEmail;
import com.example.ttversion1.login.rule.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@PasswordMatches
public class AccountDTO {
    @NotNull
    @Size(min = 6,max = 30,message = "username từ 6-30 ký tự hợp lệ")
    @ValidEmail
    private String username;
    @NotNull

    private String avatar;
    @NotNull
    @ValidPassword
    private String password;
    @NotNull
    @NotEmpty
    private String matchingPassword;

    @NotNull
    private Set<Decentralization> decentralizations;
    @NotNull
    private UserDTO userDTO;
}
