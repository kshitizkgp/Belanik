package com.backend.Belanik.post.controller;

import com.backend.Belanik.auth.config.CurrentUser;
import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.post.dto.ApiPost;
import com.backend.Belanik.post.dto.EngagePostRequest;
import com.backend.Belanik.post.dto.EngagePostResponse;
import com.backend.Belanik.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/post/{id}")
    public ApiPost getPost(@PathVariable String id) {
        // TODO(sayoni): Pass the logged in user
        return this.postService.getPostById(id, null);
    }

    @PostMapping(value = "/create_post")
    public ApiPost createPost(@RequestBody ApiPost apiPost) {
        return this.postService.createPost(apiPost);
    }

    @PutMapping(value = "/update_post/{id}")
    public ApiPost updatePost(@PathVariable String id, @RequestBody ApiPost apiPost) {
        return this.postService.updatePost(id, apiPost);
    }

    @PutMapping(value = "/engage_post/{id}")
    @PreAuthorize("hasRole('USER')")
    public EngagePostResponse engagePost(@CurrentUser LocalUser user,
                                         @PathVariable String id,
                                         @RequestBody EngagePostRequest request) {
        return this.postService.engagePost(user, id, request);
    }
}
