package com.backend.Belanik.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Media {
    @JsonProperty("video")
    String video;
    @JsonProperty("images")
    List<String> images;
}
