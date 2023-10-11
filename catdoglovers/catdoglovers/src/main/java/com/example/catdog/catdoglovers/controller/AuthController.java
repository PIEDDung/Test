package com.example.catdog.catdoglovers.controller;
import com.example.catdog.catdoglovers.dto.RegisterDto;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.service.AccountService;
import com.example.catdog.catdoglovers.util.Utility;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class AuthController {

    private AccountService accountService;
    @Autowired
    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }


    @GetMapping("/register")
    public String getRegisterForm(Model model){
        RegisterDto user = new RegisterDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegisterDto user, BindingResult result,
                           Model model, HttpSession session, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Account account = accountService.findByUsername(user.getUsername());
        if(account != null && account.getUsername() != null && !account.getUsername().isEmpty()){
            return "redirect:/register?fail";
        }
        Account existByEmail = accountService.findByEmail(user.getEmail());
        if(existByEmail != null && existByEmail.getEmail() != null && !existByEmail.getEmail().isEmpty()){
            return "redirect:/register?fail";
        }
        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "register";
        }

        accountService.saveUser(user);
        Account newAcc = accountService.findByEmail(user.getEmail());
        String siteUrl = Utility.getSiteUrl(request);
        accountService.sendVerificationCode(newAcc, siteUrl);
        model.addAttribute("user", user);
        session.setAttribute("user", user);

        return "redirect:/login";
    }

    @GetMapping("verify")
    public String getVerify(@RequestParam("code") String code){
        if(accountService.verify(code)){
            return "verify-success";
        }else{
            return "verify-fail";
        }
    }
}