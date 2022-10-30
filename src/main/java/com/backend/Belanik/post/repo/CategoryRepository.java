package com.backend.Belanik.post.repo;

import com.backend.Belanik.post.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
