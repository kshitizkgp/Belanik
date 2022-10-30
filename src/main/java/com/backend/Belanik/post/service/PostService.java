package com.backend.Belanik.post.service;

import com.backend.Belanik.post.dto.ApiPost;

public interface PostService {
    ApiPost findPostById(String id, String loggedInUserId);
}
