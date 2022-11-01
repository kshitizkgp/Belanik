package com.backend.Belanik.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;
public class PostActivity {
    @JsonProperty("count")
    long count;
    @JsonProperty("user_activity")
    List<UserActivity> userActivity;

    public PostActivity(){}

    public PostActivity(long count, List<UserActivity> userActivity) {
        this.count = count;
        this.userActivity = userActivity;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<UserActivity> getUserActivity() {
        return userActivity;
    }

    public void setUserActivity(List<UserActivity> userActivity) {
        this.userActivity = userActivity;
    }

}
