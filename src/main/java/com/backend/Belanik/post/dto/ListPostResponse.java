package com.backend.Belanik.post.dto;

import lombok.Value;

import java.util.List;

@Value
public class ListPostResponse {
    List<ApiPost> posts;
}
