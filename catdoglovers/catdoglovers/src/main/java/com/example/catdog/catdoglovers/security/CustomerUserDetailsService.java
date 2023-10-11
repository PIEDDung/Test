package com.example.catdog.catdoglovers.security;

import com.example.catdog.catdoglovers.exhandler.StatusFalseException;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    private AccountRepository accountRepository;

    @Autowired
    public CustomerUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = accountRepository.findFirstByUsername(username);

        if(user != null) {
            User authUser = new User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                            .collect(Collectors.toList())
            );
            user.setId(user.getId());
            if(!user.isStatusId()){
               throw new StatusFalseException("User is blocked");
            }
            return authUser;
        }
        else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }



}
