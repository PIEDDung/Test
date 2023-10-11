package com.example.catdog.catdoglovers.dto;

import com.example.catdog.catdoglovers.model.*;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private Account account;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String categoryName;
    private String img;

    @NotEmpty(message = "The username should not be empty")
    private String title;
    // exception


    @NotEmpty(message = "The username should not be empty")
    private String description;
    // exception

    @NotEmpty(message = "The location should not be empty")
    private String location;
    // exception


    @Min(500000L)
    @Max(100000000000L)
    private double price;
    // exception

    private Category category;
    private Date expiredDate;
    private PostStatus postStatus;

    @Transient
    public String getPhotosImagePath() {
        if (img == null || account.getId() == null) return null;

        return "/user-photos/" + id + "/" + img;
    }


}
