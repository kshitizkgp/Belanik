package com.backend.Belanik.post.controller;

import com.backend.Belanik.post.dto.ApiPost;
import com.backend.Belanik.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/post/{id}")
    public ApiPost getPost(@PathVariable String id,
                           @RequestParam(name = "user_id",required = false) String loggedInUserId) {
        // TODO(sayoni): Ideally the loggedInUserId should come from request header?
        return this.postService.getPostById(id, loggedInUserId);
    }

    @PostMapping(value = "/create_post")
    public ApiPost createPost(@RequestBody() ApiPost apiPost) {
        return this.postService.createPost(apiPost);
    }
}
