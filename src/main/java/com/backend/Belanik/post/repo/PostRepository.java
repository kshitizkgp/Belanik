package com.backend.Belanik.post.repo;

import com.backend.Belanik.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByAuthorId(String authorId);
    List<Post> findAllByOrderByLastModifiedTimestampDesc();
}
