package com.backend.Belanik.post.dto;

import lombok.Data;

@Data
public class ListPostRequest {
    public enum PostType {
        SPOTLIGHT,
        CREATED,
        SEARCH_BY_CATEGORY,
    }
    PostType type;
    String[] categoryNames;
}
