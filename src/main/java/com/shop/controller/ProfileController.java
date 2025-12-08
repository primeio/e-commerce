package com.shop.controller;

import com.shop.entity.User;
import com.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Show profile page
    @GetMapping
    public String profile(Model model) {
        User user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        return "profile/profile";
    }

    // Update profile info (username, email, phone)
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser) {

        userService.updateProfile(updatedUser);

        // Force logout to avoid session/authentication mismatch
        return "redirect:/logout";
    }



    // Show password change form
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "profile/change-password";
    }

    // Handle password change
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {
        userService.changePassword(oldPassword, newPassword, confirmPassword);
        return "redirect:/profile/change-password?success";
    }
}
