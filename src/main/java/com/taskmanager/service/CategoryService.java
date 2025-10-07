package com.taskmanager.service;

import java.util.List;

import com.taskmanager.exception.CategoryNotFoundException;
import com.taskmanager.model.Category;
import com.taskmanager.repository.CategoryRepository;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
    	if(category==null) {
    		throw new IllegalArgumentException("Category cannot be null");
    	}
    	return categoryRepository.save(category);
    }


    public Category updateCategory(String categoryId, Category updatedCategory) throws CategoryNotFoundException {
        if (!categoryRepository.exists(categoryId)) {
            throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
        }
        updatedCategory.setCategoryId(categoryId);
        return categoryRepository.save(updatedCategory);
    }

    public void deleteCategory(String categoryId) throws CategoryNotFoundException {
        if (!categoryRepository.exists(categoryId)) {
            throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    public Category getCategoryById(String categoryId) throws CategoryNotFoundException {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}


