package com.example.catdog.catdoglovers.controller;

import com.example.catdog.catdoglovers.dto.AccountDto;
import com.example.catdog.catdoglovers.dto.PostDto;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.repository.AccountRepository;
import com.example.catdog.catdoglovers.repository.CategoryRepository;
import com.example.catdog.catdoglovers.security.SecurityUtil;
import com.example.catdog.catdoglovers.service.AccountService;
import com.example.catdog.catdoglovers.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class AccountController {
    @Autowired
    private PostService postService;
    @Autowired
    private AccountService accountService;
    @GetMapping("profile/posts")
    public String getMyPosts(Model model){
        List<PostDto> posts = postService.getAccountPosts();
        model.addAttribute("posts", posts);
        return "user-posts";
    }

    @GetMapping("/profile/posts/{postId}/delete")
    public String deleteMyPost(@PathVariable(value = "postId") Long postId){
        postService.deletePost(postId);
        return "redirect:/profile/posts";
    }

    @GetMapping("/user/profile")
    public String profileUser(Model model){
        String username = SecurityUtil.getSessionUser();
        AccountDto acc = accountService.findByUsername2(username);
        model.addAttribute("user", acc);
        model.addAttribute("userId", acc.getId());
        return "user-profile";
    }

    @PostMapping("/user/profile")
    public String updateProfile(@ModelAttribute("user") AccountDto acc,
                                @RequestParam("userId") Long id,
                                Model model){
        acc.setId(id);
        AccountDto accDto = accountService.updateUserProfile(acc);
        model.addAttribute("user", accDto);
        model.addAttribute("userId", accDto.getId());
        return "user-profile";
    }
}
