package com.example.catdog.catdoglovers.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private long id;
    @NotEmpty(message = "The username should not be empty")
    private String username;
    @NotEmpty(message = "The password should not be empty")
    private String password;
    @NotEmpty(message = "The first name should not be empty")
    private String firstName;
    @NotEmpty(message = "The last name should not be empty")
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Pattern(regexp = "^[0-9]{10,12}$", message = "Phone number must be 10 to 12 digits")
    private String phoneNumber;
    @NotEmpty(message = "The email should not be empty")
    private String email;
    @NotEmpty(message = "The address should not be empty")
    private String address;

    @NotNull(message = "Please select a gender")
    private boolean gender;

    private boolean statusId;
}
