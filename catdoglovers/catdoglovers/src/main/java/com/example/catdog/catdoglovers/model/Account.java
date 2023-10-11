package com.example.catdog.catdoglovers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.security.Provider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="account")
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updatedDate;

    @Column(name = "dob")
    private Date dob;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "gender")
    private boolean gender;
    @Column(name = "hire_date")
    private Date hireDate;
    @Column(name = "status_id")
    private boolean statusId;
    @Enumerated(EnumType.STRING)
    private AuthenticationProvider authenticationProvider;

    public Account(String username, String email, AuthenticationProvider authenticationProvider) {
        this.username = username;
        this.email = email;
        this.authenticationProvider = authenticationProvider;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<RoleEntity> roles = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Post> posts = new ArrayList<>();

    private String verificationCode;
}
