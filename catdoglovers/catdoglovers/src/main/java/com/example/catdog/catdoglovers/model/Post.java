package com.example.catdog.catdoglovers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updatedDate;
    @Column(name = "img")
    private String img;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "location")
    private String location;
    @Column(name = "price")
    private double price;

    @Column(name = "expired_date")
    private Date expiredDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_order_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private PostStatus postStatus;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Order> orders = new ArrayList<>();

}


