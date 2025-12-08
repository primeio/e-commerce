package com.shop.service;

import com.shop.entity.User;
import com.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUsers(){
        return userRepository.findByStatus(User.UserStatus.ACTIVE);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User updateProfile(User updated) {
        User user = getLoggedInUser();

        user.setFullName(updated.getFullName());
        user.setPhoneNumber(updated.getPhoneNumber());
        user.setEmail(updated.getEmail());
        user.setUsername(updated.getUsername());

        return userRepository.save(user);
    }

    public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        User user = getLoggedInUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // SOFT DELETE
    public void delete(Long id) {
        User user = findById(id).orElseThrow();
        user.setStatus(User.UserStatus.DELETED);// mark deleted
        userRepository.save(user);  // save back
    }
}
