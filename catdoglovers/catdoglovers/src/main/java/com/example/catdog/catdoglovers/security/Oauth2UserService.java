package com.example.catdog.catdoglovers.security;

import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.model.AuthenticationProvider;
import com.example.catdog.catdoglovers.model.RoleEntity;
import com.example.catdog.catdoglovers.repository.AccountRepository;
import com.example.catdog.catdoglovers.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class Oauth2UserService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

    public void processAfterGoogleLogin(String email, String username){
        Account existAccount = accountRepository.findByUsername(username);
        if(existAccount == null) {
            Account account = new Account();
            account.setEmail(email);
            account.setUsername(username);
            account.setAuthenticationProvider(AuthenticationProvider.GOOGLE);
            RoleEntity roles = roleRepository.findByRoleName("USER");
            account.setRoles(Arrays.asList(roles));
            account.setStatusId(true);
            accountRepository.save(account);
        }
    }
}
