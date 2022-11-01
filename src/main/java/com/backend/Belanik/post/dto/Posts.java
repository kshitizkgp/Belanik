package com.backend.Belanik.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class Posts {
    @JsonProperty("post_id")
    Set<String> postId;

    public Posts(){}

    public Posts(Set<String> postId) {
        this.postId = postId;
    }

    public Set<String> getPostId() {
        return postId;
    }

    public void setPostId(Set<String> postId) {
        this.postId = postId;
    }
}
