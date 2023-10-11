package com.example.catdog.catdoglovers.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    @NotEmpty(message = "The username should not be empty !")
    private String username;
    @NotEmpty(message = "The password should not be empty !")
    private String password;
    @NotEmpty(message = "The first name should not be empty !")
    private String firstName;
    @NotEmpty(message = "The last name should not be empty !")
    private String lastName;

    private LocalDateTime createDate;
    private LocalDateTime updatedDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    @NotEmpty(message = "The phone number should not be empty !")
    private String phoneNumber;
    @NotEmpty(message = "The email should not be empty !")
    private String email;
    @NotEmpty(message = "The address should not be empty !")
    private String address;
    private boolean gender;

    private Date hireDate;

    private int statusId;

}
