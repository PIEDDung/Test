package com.example.catdog.catdoglovers.repository;

import com.example.catdog.catdoglovers.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRoleName(String name);
}
