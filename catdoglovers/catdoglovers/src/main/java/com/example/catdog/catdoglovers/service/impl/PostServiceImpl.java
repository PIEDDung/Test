package com.example.catdog.catdoglovers.service.impl;

import com.example.catdog.catdoglovers.dto.PostDto;
import com.example.catdog.catdoglovers.exhandler.AccountException;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.model.Category;
import com.example.catdog.catdoglovers.model.Post;
import com.example.catdog.catdoglovers.model.PostStatus;
import com.example.catdog.catdoglovers.repository.AccountRepository;
import com.example.catdog.catdoglovers.repository.CategoryRepository;
import com.example.catdog.catdoglovers.repository.PostRepository;
import com.example.catdog.catdoglovers.repository.PostStatusRepository;
import com.example.catdog.catdoglovers.security.SecurityUtil;
import com.example.catdog.catdoglovers.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
//    private ModelMapper modelMapper;
    private AccountRepository accountRepository;

    private PostRepository postRepository;

    private CategoryRepository categoryRepository;


    private PostStatusRepository postStatusRepository;
    @Autowired
    public PostServiceImpl(AccountRepository accountRepository, PostRepository postRepository, CategoryRepository categoryRepository, PostStatusRepository postStatusRepository) {
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.postStatusRepository = postStatusRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto, Long categoryId) {
        String username = SecurityUtil.getSessionUser();
        Account acc = accountRepository.findByUsername(username);
        Post post = mapToEntity(postDto);
        post.setAccount(acc);
        Long id = 2L;
        PostStatus postStatus = postStatusRepository.findById(id).get();
        post.setPostStatus(postStatus);
        Category category = categoryRepository.findById(categoryId).get();
        post.setCategory(category);
        Post newPost = postRepository.save(post);
        return mapToDto(newPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long categoryId) {
        String username = SecurityUtil.getSessionUser();
        Account acc = accountRepository.findByUsername(username);
        Post post = mapToEntity(postDto);
        post.setAccount(acc);
        Long id = 2L;
        PostStatus postStatus = postStatusRepository.findById(id).get();
        post.setPostStatus(postStatus);
        Category category = categoryRepository.findById(categoryId).get();
        post.setCategory(category);
        Post editPost = postRepository.save(post);
        return mapToDto(editPost);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post>posts = postRepository.findAllPostActive();
        List<PostDto> postsDto = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        return postsDto;
    }



    @Override
    public List<PostDto> getAccountPosts() {
        String username = SecurityUtil.getSessionUser();
        List<Post> accountPosts = postRepository.findPostFromAccount(username);
        return accountPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).get();
        postRepository.delete(post);
    }

    @Override
    public PostDto findPostById(Long postId) {
        Post post = postRepository.findById(postId).get();
        return mapToDto(post);
    }

    @Override
    public List<PostDto> searchPost(String query, String username) {
        List<Post> posts = postRepository.searchClub(query, username);
        return posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
    }

    private PostDto mapToDto(Post post){
        return PostDto.builder()
                .id(post.getId())
                .createdDate(post.getCreatedDate())
                .updatedDate(post.getUpdatedDate())
                .img(post.getImg())
                .title(post.getTitle())
                .description(post.getDescription())
                .location(post.getLocation())
                .price(post.getPrice())
                .expiredDate(post.getExpiredDate())
                .account(post.getAccount())
                .category(post.getCategory())
                .postStatus(post.getPostStatus())
                .build();
    }

    private Post mapToEntity(PostDto postDto){
        return Post.builder()
                .id(postDto.getId())
                .createdDate(postDto.getCreatedDate())
                .updatedDate(postDto.getUpdatedDate())
                .img(postDto.getImg())
                .title(postDto.getTitle())
                .description(postDto.getDescription())
                .location(postDto.getLocation())
                .price(postDto.getPrice())
                .expiredDate(postDto.getExpiredDate())
                .account(postDto.getAccount())
                .category(postDto.getCategory())
                .postStatus(postDto.getPostStatus())
                .build();
    }

}
