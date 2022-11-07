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
    public ApiPost getPost(@PathVariable String id, @CurrentUser LocalUser localUser) {
        return this.postService.getPostById(id, localUser);
    }

    @PostMapping(value = "/create_post")
    @PreAuthorize("hasRole('USER')")
    public ApiPost createPost(@RequestBody ApiPost apiPost, @CurrentUser LocalUser localUser) {
        return this.postService.createPost(apiPost, localUser);
    }

    @PutMapping(value = "/update_post/{id}")
    @PreAuthorize("hasRole('USER')")
    public ApiPost updatePost(@PathVariable String id,
                              @RequestBody ApiPost apiPost,
                              @CurrentUser LocalUser localUser) {
        return this.postService.updatePost(id, apiPost, localUser);
    }

    @PutMapping(value = "/engage_post/{id}")
    @PreAuthorize("hasRole('USER')")
    public EngagePostResponse engagePost(@CurrentUser LocalUser user,
                                         @PathVariable String id,
                                         @RequestBody EngagePostRequest request) {
        return this.postService.engagePost(user, id, request);
    }
}
