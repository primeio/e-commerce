package com.shop.service;

import com.shop.entity.Category;
import com.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

   public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
   public Category getCategoryById(long id){
        return categoryRepository.findById(id).
                orElseThrow();
    }
    public Category saveCategory(Category category){
       return categoryRepository.save(category);
    }
    public void deleteCategory(long id){
       categoryRepository.deleteById(id);
    }

}
