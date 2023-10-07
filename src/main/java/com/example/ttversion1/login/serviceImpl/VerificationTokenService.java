package com.example.ttversion1.login.serviceImpl;

import com.example.ttversion1.login.entity.VerificationToken;
import com.example.ttversion1.login.repository.VerificationTokenRepository;
import com.example.ttversion1.login.service.IVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class VerificationTokenService implements IVerificationTokenService {
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingToken) {
        VerificationToken oldToken = tokenRepository.findByToken(existingToken);
        if (oldToken == null) {

            return null;
        }

        // Generate a new token
        String newTokenString = UUID.randomUUID().toString();
        VerificationToken newToken = new VerificationToken(newTokenString, oldToken.getAccount());
        newToken.setExpiryDate(calculateExpiryDate(VerificationToken.EXPIRATION)); // Set the expiry date

        // Save the new token
        tokenRepository.save(newToken);

        // Delete the old token
        tokenRepository.delete(oldToken);

        return newToken;
    }
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return calendar.getTime();
    }
}
