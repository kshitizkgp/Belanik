package com.backend.Belanik.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

// TODO(kshitiz): Add annotation Getter, Setter, NoArgsConstructor
public class PostIds {
    @JsonProperty("post_id")
    Set<String> postId;

    public PostIds(){}

    public PostIds(Set<String> postId) {
        this.postId = postId;
    }

    public Set<String> getPostId() {
        return postId;
    }

    public void setPostId(Set<String> postId) {
        this.postId = postId;
    }
}
