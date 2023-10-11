package com.example.catdog.catdoglovers.repository;

import com.example.catdog.catdoglovers.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    @Query(value = "select c.name from category c", nativeQuery = true)
//    List<Category> getCategoryName();
    @Query(value = "select c.id from category c where c.name = :name", nativeQuery = true)
    Long getCategoryIdByName(String name);
}
