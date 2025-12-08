package com.shop.controller;



import com.shop.entity.User;
import com.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    // LIST USERS
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    // SHOW ADD PAGE
    @GetMapping("/add")
    public String addUserPage(Model model) {
        model.addAttribute("user", new User());
        return "admin/add-user";
    }

    // ADD USER
    @PostMapping("/add")
    public String addUserSubmit(@ModelAttribute User user) {
        userService.save(user);  // You must have save() in repo
        return "redirect:/admin/users";
    }

    // SHOW EDIT PAGE
    @GetMapping("/edit/{id}")
    public String editUserPage(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id).orElseThrow());
        return "admin/edit-user";
    }

    // UPDATE USER
    @PostMapping("/update")
    public String updateUserSubmit(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/admin/users";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }


}
