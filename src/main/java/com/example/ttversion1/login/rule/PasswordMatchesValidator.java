package com.example.ttversion1.login.rule;


import com.example.ttversion1.login.dto.AccountDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, AccountDTO> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(AccountDTO accountDTO, ConstraintValidatorContext context) {
        return accountDTO.getPassword().equals(accountDTO.getMatchingPassword());
    }
}
