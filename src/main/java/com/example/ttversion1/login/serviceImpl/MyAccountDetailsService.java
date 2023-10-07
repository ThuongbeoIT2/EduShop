package com.example.ttversion1.login.serviceImpl;

import com.example.ttversion1.login.entity.Account;
import com.example.ttversion1.login.entity.Decentralization;
import com.example.ttversion1.login.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MyAccountDetailsService implements UserDetailsService {
   @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    public MyAccountDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {

        Account account = accountRepo.findByUsername(username).orElseThrow(() ->
                new RuntimeException("User not found: " + username));

        List<GrantedAuthority> authorities =  getUserAuthority(account.getRoles());
        return buildUserForAuthentication(account, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Decentralization> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (Decentralization role : userRoles) {
            roles.add(new SimpleGrantedAuthority(role.getAuthorityname()));
        }
        return new ArrayList<>(roles);
    }

    private UserDetails buildUserForAuthentication(Account account,List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                account.getUsername(), account.getPassword(),
                account.isEnabled(), true, true, true, authorities);
    }
}
