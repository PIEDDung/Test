package com.example.catdog.catdoglovers.controller;

import com.example.catdog.catdoglovers.dto.PostDto;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.model.Category;
import com.example.catdog.catdoglovers.model.Post;
import com.example.catdog.catdoglovers.repository.AccountRepository;
import com.example.catdog.catdoglovers.repository.CategoryRepository;
import com.example.catdog.catdoglovers.security.SecurityUtil;
import com.example.catdog.catdoglovers.service.AccountService;
import com.example.catdog.catdoglovers.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String getHome(){
        return "posts";
    }

    @GetMapping("posts")
    public String listPosts(Model model){
        List<PostDto> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts";
    }



    @GetMapping("/posts/create")
    public String createPage(Model model){
        Post post = new Post();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("post", post);
        return "posts-create";
    }

    @GetMapping("/posts/{postId}/edit")
    public String editForm(@PathVariable("postId") Long postId, Model model){
        PostDto post = postService.findPostById(postId);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("post", post);
        return "posts-edit";
    }

    @GetMapping("/posts/search")
    public String searchPost(@RequestParam(value = "query") String query, Model model){

//        Account acc = new Account();
        String username = SecurityUtil.getSessionUser();
//        if(username != null){
//            acc = accountService.findByUsername(username);
//            model.addAttribute("acc", acc);
//        }
        List<PostDto> post = postService.searchPost(query, username);
        model.addAttribute("posts", post);
        return "user-posts";
    }

    @PostMapping("/posts/{postId}/edit")
    public String updatePost(@PathVariable("postId") Long postId,@Valid @ModelAttribute("post") PostDto postDto,
                             @RequestParam("image") MultipartFile multipartFile,
                             @RequestParam("category") Long categoryId,
                             BindingResult result, Model model) throws IOException {
        if(result.hasErrors()){
            model.addAttribute("post", postDto);
            return "posts-edit";
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        postDto.setImg(fileName);

        postDto.setId(postId);


        postDto.setImg(fileName);
        PostDto savedPost = postService.updatePost(postDto,categoryId);

        String uploadDir = "./user-photos/" + savedPost.getId();

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception ex){
            throw new IOException("Could not save uploaded file" + fileName);
        }
        return "redirect:/profile/posts";
    }

    @PostMapping("/posts/create")
    public String savePost(@Valid @ModelAttribute("post") PostDto postDto,
                           @RequestParam("image") MultipartFile multipartFile,
                           @RequestParam("category") Long categoryId,
                           BindingResult result, Model model) throws Exception {


        if(result.hasErrors()){
            model.addAttribute("post", postDto);
            return "posts-create";
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        postDto.setImg(fileName);

        PostDto savedPost = postService.createPost(postDto, categoryId);

        String uploadDir = "./user-photos/" + savedPost.getId();

        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception ex){
            throw new IOException("Could not save uploaded file" + fileName);
        }

        return "redirect:/posts";
    }


}
