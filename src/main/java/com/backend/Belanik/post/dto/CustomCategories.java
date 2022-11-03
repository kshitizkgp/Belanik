package com.backend.Belanik.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public class CustomCategories {
    @JsonProperty("category_name")
    List<String> categoryNames;
}
