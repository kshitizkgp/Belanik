package com.backend.Belanik.post.service;

import com.backend.Belanik.post.dto.ApiCategory;
import com.backend.Belanik.post.model.Category;
import com.backend.Belanik.post.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    public List<ApiCategory> listCategories() {
        List<Category> categories = categoryRepository.findAll();
        // Create map of category id to Category. This will be used to convert parent category id to name
        Map<String, Category> idToCategoryMap = new HashMap<>();
        for (Category category: categories) {
            idToCategoryMap.put(category.getCategoryId(), category);
        }
        List<ApiCategory> apiCategories = new ArrayList<>();

        // Create list
        for (Category category: categories) {
            String parentName = null;
            Category parentCategory = idToCategoryMap.get(category.getParent());
            // This logic assumes that there will be only one-level deep parent
            if (parentCategory != null) {
                parentName = parentCategory.getName();
            }
            ApiCategory apiCategory = new ApiCategory(category.getCategoryId(),
                    category.getName(),
                    parentName);
            apiCategories.add(apiCategory);
        }
        return apiCategories;
    }
}
