package com.example.catdog.catdoglovers.repository;

import com.example.catdog.catdoglovers.dto.PostDto;
import com.example.catdog.catdoglovers.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select p.* from post p where p.status_id = '1'", nativeQuery = true)
    List<Post>findAllPostActive();
    @Query(value = "select p.* from post p\n" +
            "join account a on p.account_id = a.id \n" +
            "where a.username = :username", nativeQuery = true)
    List<Post>findPostFromAccount(String username);


    @Query(value = "select p.* from post p\n" +
                       "join account a on p.account_id = a.id\n" +
                       "where a.username = :username and p.title LIKE CONCAT('%', :query, '%')", nativeQuery = true)
    List<Post> searchClub(String query, String username);

    @Query(value = "select * from post where account_id = :accountId", nativeQuery = true)
    Page<Post> findByAccountId(Long accountId, Pageable pageable);

}
