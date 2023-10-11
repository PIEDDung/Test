package com.example.catdog.catdoglovers.service.impl;

import com.example.catdog.catdoglovers.dto.AccountDto;
import com.example.catdog.catdoglovers.dto.RegisterDto;
import com.example.catdog.catdoglovers.exhandler.AccountException;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.model.RoleEntity;
import com.example.catdog.catdoglovers.repository.AccountRepository;
import com.example.catdog.catdoglovers.repository.RoleRepository;
import com.example.catdog.catdoglovers.security.SecurityUtil;
import com.example.catdog.catdoglovers.service.AccountService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                              JavaMailSender mailSender) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public AccountDto findById(Long id) {
        Account acc = accountRepository.findById(id).orElseThrow(() -> new AccountException("Not found account"));
        AccountDto accountDto = this.modelMapper.map(acc, AccountDto.class);
        return accountDto;
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public AccountDto findByUsername2(String username) {
        Account acc = accountRepository.findByUsername(username);
        AccountDto accountDto = this.modelMapper.map(acc, AccountDto.class);
        return accountDto;
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


    @Override
    public Account saveUser(RegisterDto registerDto) {
        Account account = new Account();
        account.setUsername(registerDto.getUsername());
        account.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        account.setEmail(registerDto.getEmail());
        account.setFirstName(registerDto.getFirstName());
        account.setLastName(registerDto.getLastName());
        account.setAddress(registerDto.getAddress());
        RoleEntity roles = roleRepository.findByRoleName("USER");
        account.setRoles(Arrays.asList(roles));
        account.setGender(registerDto.isGender());
        account.setPhoneNumber(registerDto.getPhoneNumber());
        account.setDob(registerDto.getDob());
        account.setStatusId(false);
        String verificationCode = RandomString.make(64);
        account.setVerificationCode(verificationCode);
        return accountRepository.save(account);
    }

    @Override
    public AccountDto updateUserProfile(AccountDto accountDto) {
        Account acc = maptoEntity(accountDto);
        acc.setStatusId(true);
        Account accEdit = accountRepository.save(acc);
        return maptoDto(accEdit);
    }


    public void sendVerificationCode(Account account, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please Verify Your Registration";
        String senderName = "CatDogLovers";
        String mailContent = "<p>Dear "  + account.getUsername() + ",</p>";
        mailContent += "<p>Please Click the link below to verify</p>";
        String verifyUrl = siteUrl + "/verify?code=" + account.getVerificationCode();
        mailContent += "<h3><a href ='" + verifyUrl + "'>Verify</a></h3>";
        mailContent += "<p>Thank you</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("nambtse172988@fpt.edu.vn", senderName);
        helper.setTo(account.getEmail());
        helper.setSubject(subject);

        helper.setText(mailContent, true);
        mailSender.send(message);
    }

    @Override
    public boolean verify(String code) {
        Account account = accountRepository.findByVerificationCode(code);
        if(account == null || account.isStatusId()){
            return false;
        }else{
            account.setVerificationCode(null);
            account.setStatusId(true);
            accountRepository.save(account);
            return true;
        }
    }

    @Override
    public Account findByAccountId(Long id) {
        Account acc = accountRepository.findById(id).get();
        return acc;
    }


    public AccountDto maptoDto(Account acc){
        return AccountDto.builder()
                .id(acc.getId())
                .username(acc.getUsername())
                .password(acc.getPassword())
                .firstName(acc.getFirstName())
                .lastName(acc.getLastName())
                .createDate(acc.getCreatedDate())
                .updatedDate(acc.getUpdatedDate())
                .dob((acc.getDob()))
                .phoneNumber(acc.getPhoneNumber())
                .email(acc.getEmail())
                .address(acc.getAddress())
                .gender(acc.isGender())
                .build();
    }

    public Account maptoEntity(AccountDto acc){
        return Account.builder()
                .id(acc.getId())
                .username(acc.getUsername())
                .password(acc.getPassword())
                .firstName(acc.getFirstName())
                .lastName(acc.getLastName())
                .createdDate(acc.getCreateDate())
                .updatedDate(acc.getUpdatedDate())
                .dob((acc.getDob()))
                .phoneNumber(acc.getPhoneNumber())
                .email(acc.getEmail())
                .address(acc.getAddress())
                .gender(acc.isGender())
                .build();
    }

}
