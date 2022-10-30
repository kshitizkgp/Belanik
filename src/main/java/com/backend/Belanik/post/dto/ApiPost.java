package com.backend.Belanik.post.dto;

import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class ApiPost {
    String postId;
    String authorName;
    String title;
    List<String> categoryNames;
    List<String> customCategoryNames;
    String video;
    List<String> imageUrls;
    String description;
    int likeCount;
    Date lastModifiedTimestamp;
    boolean userLiked;
}
