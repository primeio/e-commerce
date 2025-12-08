package com.shop.service;

import com.shop.entity.Product;
import com.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productsRepository;

    public Page<Product> getAllProducts(
            int page, int size, String keyword,
            String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return productsRepository
                    .findByNameContainingIgnoreCaseAndDeletedFalse(keyword, pageable);
        }

        return productsRepository.findByDeletedFalse(pageable);
    }

    public Product getProduct(Long id) {
        Product product = productsRepository.findById(id).orElseThrow();
        if (product.isDeleted())
            throw new RuntimeException("Product not found");

        return product;
    }

    public Product saveProduct(Product product) {
        return productsRepository.save(product);
    }

    // ⭐ Soft Delete — NOT removing from database
    public void deleteProduct(Long id) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setDeleted(true);  // soft delete
        productsRepository.save(product);
    }

    public Page<Product> getProductsByCategory(
            String category, int page, int size,
            String sortField, String sortDir) {

        Sort sort = sortDir.equals("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productsRepository
                .findByCategoryNameIgnoreCaseAndDeletedFalse(category, pageable);
    }

    public List<Product> allProducts() {
        return productsRepository.findByDeletedFalse();
    }
}
