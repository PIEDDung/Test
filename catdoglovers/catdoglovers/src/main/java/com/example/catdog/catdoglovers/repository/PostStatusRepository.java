package com.example.catdog.catdoglovers.repository;

import com.example.catdog.catdoglovers.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStatusRepository extends JpaRepository<PostStatus, Long> {
}
