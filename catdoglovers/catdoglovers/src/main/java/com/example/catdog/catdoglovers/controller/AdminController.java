package com.example.catdog.catdoglovers.controller;

import com.example.catdog.catdoglovers.dto.PostDto;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.service.AccountService;
import com.example.catdog.catdoglovers.service.AdminService;
import com.example.catdog.catdoglovers.service.PostService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PostService postService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    @RolesAllowed("ADMIN")
    public String getAdmin(){
        return "admin";
    }

    @GetMapping("/admin/posts")
    @RolesAllowed("ADMIN")
    public String getAdminPost(Model model){
//        List<PostDto> posts = adminService.getAdminPosts();
//        model.addAttribute("posts", posts);
//        return "admin-posts";
        return findPaginated(1, "title", "asc", model);
    }

    @GetMapping("/admin/posts/pageNo={pageNo}")
    @RolesAllowed("ADMIN")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String field,
                                @RequestParam("sortDirection") String direction,
                                Model model){
        int pageSize = 5;

        Page<PostDto> page = adminService.findPostPaginated(pageNo, pageSize, field, direction);
        List<PostDto> listPosts = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalPosts", page.getTotalElements());

        model.addAttribute("sortField", field);
        model.addAttribute("sortDirection", direction);
        model.addAttribute("reserveSortDir", direction.equals("asc") ? "desc" : "asc");
        model.addAttribute("posts", listPosts);
        return "admin-posts";
    }

    @GetMapping("/admin/{postId}/accept")
    @RolesAllowed("ADMIN")
    public String acceptPost(@PathVariable(value = "postId") Long postId){
        adminService.setStatusToPost(postId, 1L);
        return "redirect:/admin/posts";
    }

    @GetMapping("/admin/{postId}/decline")
    @RolesAllowed("ADMIN")
    public String declinePost(@PathVariable(value = "postId") Long postId){
        adminService.setStatusToPost(postId, 3L);
        return "redirect:/admin/posts";
    }

    @GetMapping("/admin/members")
    @RolesAllowed("ADMIN")
    public String memberManage(Model model){
//        List<Account> accountList = adminService.getAllAccount();
//        model.addAttribute("accounts", accountList);
        return findMembersPaginated(1, "username", "asc", model);
    }

    @GetMapping("/admin/member/{accountId}/detail")
    @RolesAllowed("ADMIN")
    public String memberDetails(@PathVariable(value = "accountId") Long accountId,
                                Model model){
//        Account account = accountService.findByAccountId(accountId);
//
//        model.addAttribute("account", account);
        return findAccountPostPaginated(1, accountId, "title", "asc", model);
    }

    @GetMapping("/admin/member/accountId{accountId}/detail/posts/pageNo={pageNo}")
    @RolesAllowed("ADMIN")
    public String findAccountPostPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @PathVariable(value = "accountId") Long accountId,
                                @RequestParam("sortField") String field,
                                @RequestParam("sortDirection") String direction,
                                Model model){
        int pageSize = 3;
        Account account = accountService.findByAccountId(accountId);
        Page<PostDto> page = adminService.findPostOfAccountPaginated(pageNo, pageSize, field, direction, accountId);
        List<PostDto> listPosts = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalPosts", page.getTotalElements());
        model.addAttribute("account", account);
        model.addAttribute("sortField", field);
        model.addAttribute("sortDirection", direction);
        model.addAttribute("reserveSortDir", direction.equals("asc") ? "desc" : "asc");
        model.addAttribute("posts", listPosts);
        return "admin-members-detail";
    }

    @GetMapping("/admin/members/pageNo={pageNo}")
    @RolesAllowed("ADMIN")
    public String findMembersPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String field,
                                @RequestParam("sortDirection") String direction,
                                Model model){
        int pageSize = 2;

        Page<Account> page = adminService.findAccountPaginated(pageNo, pageSize, field, direction);
        List<Account> listAccounts = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalPosts", page.getTotalElements());

        model.addAttribute("sortField", field);
        model.addAttribute("sortDirection", direction);
        model.addAttribute("reserveSortDir", direction.equals("asc") ? "desc" : "asc");

//        List<Account> nonAdminAccounts = listAccounts.stream()
//                .filter(account -> account.getRoles().stream()
//                        .noneMatch(role -> role.getRoleName().equals("ADMIN")))
//                .collect(Collectors.toList());

        List<Account> filteredAccounts = listAccounts.stream()
                .filter(account -> account.getRoles().stream().anyMatch(role -> "USER".equals(role.getRoleName())))
                .collect(Collectors.toList());

//        model.addAttribute("accounts", listAccounts);
        model.addAttribute("filteredAccounts", filteredAccounts);
        return "admin-members";
    }

    @GetMapping("/admin/member/{postId}/accept")
    @RolesAllowed("ADMIN")
    public String acceptMemberPost(@PathVariable(value = "postId") Long postId,
                                   @RequestParam("accountId") Long accountId){
        adminService.setStatusToPost(postId, 1L);
        return "redirect:/admin/member/"+accountId+"/detail";
    }

    @GetMapping("/admin/member/{postId}/decline")
    @RolesAllowed("ADMIN")
    public String declineMemberPost(@PathVariable(value = "postId") Long postId,
                                    @RequestParam("accountId") Long accountId){
        adminService.setStatusToPost(postId, 3L);
        return "redirect:/admin/member/"+accountId+"/detail";
    }

}
