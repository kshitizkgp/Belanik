package com.backend.Belanik.post.service;

import com.backend.Belanik.post.dto.ApiPost;

public interface PostService {
    ApiPost getPostById(String id, String loggedInUserId);
    ApiPost createPost(ApiPost apiPost);
    ApiPost updatePost(String id, ApiPost apiPost);
}
