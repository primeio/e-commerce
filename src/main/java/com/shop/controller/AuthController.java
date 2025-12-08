package com.shop.controller;

import com.shop.entity.Product;
import com.shop.entity.Role;
import com.shop.entity.User;
import com.shop.repository.RoleRepository;
import com.shop.repository.UserRepository;
import com.shop.service.CategoryService;
import com.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;


@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ProductService productService;
    private final CategoryService categoryService;


    @GetMapping("/")
    public String home() {
        return "redirect:/user/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }



    @GetMapping("/register")
    public String register(Model model) {
        User user= new User();
        model.addAttribute("user", user);
        return "register";

    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Fetch role from DB
        Role role = roleRepository.findByName("USER");

        // Handle case when role not found
        if (role == null) {
            role = new Role();
            role.setName("USER");
            roleRepository.save(role);
        }

        // Assign role to user
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        // Save user
        userRepository.save(user);

        return "redirect:/login?register";
    }
    @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "name") String sortField,
                                @RequestParam(defaultValue = "asc") String sortDir,
                                @RequestParam(defaultValue = "") String keyword,
                                @RequestParam(required = false) String category,
                                Model model) {

        Page<Product> productPage;

        // 🔥 1️⃣ Category filter has higher priority
        if (category != null && !category.isEmpty()) {
            productPage = productService.getProductsByCategory(category, page, size, sortField, sortDir);
        }
        // 🔥 2️⃣ Keyword search
        else if (!keyword.isEmpty()) {
            productPage = productService.getAllProducts(page, size, keyword, sortField, sortDir);
        }
        // 🔥 3️⃣ Default: show all products
        else {
            productPage = productService.getAllProducts(page, size, "", sortField, sortDir);
        }

        // Common model attributes
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        // Add categories to view
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("selectedCategory", category);  // useful for highlighting active category

        return "user/dashboard";
    }





}
