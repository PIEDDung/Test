package com.example.catdog.catdoglovers.service;

import com.example.catdog.catdoglovers.dto.PostDto;
import com.example.catdog.catdoglovers.model.Post;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto, Long categoryId);

    PostDto updatePost(PostDto postDto, Long categoryId);
    List<PostDto> getAllPosts();

    List<PostDto> getAccountPosts();
    void deletePost(Long postId);

    PostDto findPostById(Long postId);

    List<PostDto> searchPost(String query, String username);
}
