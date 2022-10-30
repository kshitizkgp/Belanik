package com.backend.Belanik.post.controller;

import com.backend.Belanik.post.dto.ApiCategory;
import com.backend.Belanik.post.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public List<ApiCategory> listCategories() {
        return this.categoryService.listCategories();
    }
}
