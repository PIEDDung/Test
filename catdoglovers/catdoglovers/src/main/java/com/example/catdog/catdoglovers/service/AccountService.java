package com.example.catdog.catdoglovers.service;

import com.example.catdog.catdoglovers.dto.AccountDto;
import com.example.catdog.catdoglovers.dto.RegisterDto;
import com.example.catdog.catdoglovers.model.Account;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

public interface AccountService {
    AccountDto findById(Long id);
    Account findByAccountId(Long id);

    Account findByUsername(String username);
    AccountDto findByUsername2(String username);
    Account findByEmail(String email);
    Account saveUser(RegisterDto registerDto);

    AccountDto updateUserProfile(AccountDto acc);

    void sendVerificationCode(Account account, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String code);

}
