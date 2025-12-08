package com.shop.controller;

import com.shop.entity.Category;
import com.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String allCategory(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories/list";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createCategory(Model model){
        model.addAttribute("category", new Category());
        return "categories/add";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category,Model model) {

        model.addAttribute("category",categoryService.saveCategory(category));
        return "redirect:/categories";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model){
        model.addAttribute("category",categoryService.getCategoryById(id));
        return "categories/edit";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

}
