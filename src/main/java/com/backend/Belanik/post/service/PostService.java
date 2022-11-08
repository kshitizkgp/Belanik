package com.backend.Belanik.post.service;

import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.post.dto.*;

public interface PostService {
    ApiPost getPostById(String id, LocalUser currentUser);
    ListPostResponse listPosts(ListPostRequest listPostRequest, LocalUser currentUser);
    ApiPost createPost(ApiPost apiPost, LocalUser currentUser);
    ApiPost updatePost(String id, ApiPost apiPost, LocalUser currentUser);
    EngagePostResponse engagePost(LocalUser currentUser, String post_id, EngagePostRequest request);
}
