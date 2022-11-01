package com.backend.Belanik.post.service;

import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.post.dto.ApiPost;
import com.backend.Belanik.post.dto.EngagePostRequest;
import com.backend.Belanik.post.dto.EngagePostResponse;

public interface PostService {
    ApiPost getPostById(String id, String loggedInUserId);
    ApiPost createPost(ApiPost apiPost);
    ApiPost updatePost(String id, ApiPost apiPost);

    EngagePostResponse engagePost(LocalUser currentUser, String post_id, EngagePostRequest request);
}
