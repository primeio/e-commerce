package com.shop.controller;

import com.shop.entity.Product;
import com.shop.service.CategoryService;
import com.shop.service.ImageUploadService;
import com.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageUploadService imageUploadService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String getAllProducts(Model model){
        model.addAttribute("products", productService.allProducts());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "product/list";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable long id, Model model){
        model.addAttribute("product", productService.getProduct(id));
        return "product-detail";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createProduct(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "product/add";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveProduct(@RequestParam(defaultValue = "imageFile") MultipartFile imageFile,
                              @ModelAttribute Product product) throws Exception {
        if (!imageFile.isEmpty()){
        String imageUrl = imageUploadService.uploadImage(imageFile,"products");
        product.setImageUrl(imageUrl);
        }
        productService.saveProduct(product);

        return "redirect:/products";

    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable long id, Model model){
        Product existing = productService.getProduct(id);
        model.addAttribute("product", existing);
        model.addAttribute("categories", categoryService.getAllCategory());
        return "product/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);  // soft delete
        return "redirect:/products?deleted";
    }


}
