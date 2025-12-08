package com.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String state;
    private String pinCode;
    private String city;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;
    public enum UserStatus {
        ACTIVE,
        DELETED,
        BLOCKED,
        INACTIVE
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<Cart> carts= new ArrayList<>();
}
