package com.backend.Belanik.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostActivity {
    @JsonProperty("count")
    long count;
    @JsonProperty("user_activity")
    List<UserActivity> userActivity;

    public PostActivity(long count, List<UserActivity> userActivity) {
        this.count = count;
        this.userActivity = userActivity;
    }
}
