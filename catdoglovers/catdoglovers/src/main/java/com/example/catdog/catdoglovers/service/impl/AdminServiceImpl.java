package com.example.catdog.catdoglovers.service.impl;

import com.example.catdog.catdoglovers.dto.PostDto;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.model.Post;
import com.example.catdog.catdoglovers.model.PostStatus;
import com.example.catdog.catdoglovers.repository.AccountRepository;
import com.example.catdog.catdoglovers.repository.PostRepository;
import com.example.catdog.catdoglovers.repository.PostStatusRepository;
import com.example.catdog.catdoglovers.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PostStatusRepository postStatusRepository;

    @Autowired
    private PostRepository postRepository;
    @Override
    public List<PostDto> getAdminPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public PostDto setStatusToPost(Long postId, Long statusId) {
        Post post = postRepository.findById(postId).get();
        PostStatus postStatus = postStatusRepository.findById(statusId).get();
        post.setPostStatus(postStatus);
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public List<Account> getAllAccount() {
        Long accountId = accountRepository.getUserAccountId();
        List<Account> accounts = accountRepository.findById(accountId).stream().toList();
        return accounts;
    }

    @Override
    public Page<PostDto> findPostPaginated(int pageNo, int pageSize, String field, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending()
                :Sort.by(field).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::mapToDto);
    }

    @Override
    public Page<Account> findAccountPaginated(int pageNo, int pageSize, String field, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending()
                :Sort.by(field).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return accountRepository.findAll(pageable);
    }

    @Override
    public Page<PostDto> findPostOfAccountPaginated(int pageNo, int pageSize, String field, String direction, Long accountId) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending()
                :Sort.by(field).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Post> listPosts = postRepository.findByAccountId(accountId, pageable);
        return listPosts.map(this::mapToDto);
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
