package com.shop.repository;

import com.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByDeletedFalse(Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndDeletedFalse
            (String keyword, Pageable pageable);

    Page<Product> findByCategoryNameIgnoreCaseAndDeletedFalse
            (String category, Pageable pageable);

    List<Product> findByDeletedFalse();
}

