package com.backend.Belanik.post.dto;

import lombok.Value;

@Value
public class ApiCategory {
    String categoryId;
    String categoryName;
    String parentName;
}
