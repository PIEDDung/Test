package com.example.catdog.catdoglovers.service;

import com.example.catdog.catdoglovers.dto.PostDto;
import com.example.catdog.catdoglovers.model.Account;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    List<PostDto> getAdminPosts();

    PostDto setStatusToPost(Long postId, Long statusId);

    List<Account> getAllAccount();

    Page<PostDto> findPostPaginated(int pageNo, int pageSize, String field, String direction);

    Page<Account> findAccountPaginated(int pageNo, int pageSize, String field, String direction);

    Page<PostDto> findPostOfAccountPaginated(int pageNo, int pageSize, String field, String direction, Long accountId);

}
