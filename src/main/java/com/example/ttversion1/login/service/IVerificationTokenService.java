package com.example.ttversion1.login.service;

import com.example.ttversion1.login.entity.VerificationToken;

public interface IVerificationTokenService {
    VerificationToken getVerificationToken(String verificationToken);
    VerificationToken generateNewVerificationToken(String existingToken);
}
