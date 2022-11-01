package com.backend.Belanik.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class UserActivity {
    @JsonProperty("user_id")
    String userId;
    @JsonProperty("modified_timestamp")
    String modifiedTimestamp;

    public UserActivity() {}

    public UserActivity(String userId, String modifiedTimestamp) {
        this.userId = userId;
        this.modifiedTimestamp = modifiedTimestamp;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(String modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }
}
