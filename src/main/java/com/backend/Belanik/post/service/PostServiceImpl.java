package com.backend.Belanik.post.service;

import com.backend.Belanik.post.dto.ApiPost;
import com.backend.Belanik.post.model.Post;
import com.backend.Belanik.post.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostServiceImpl implements PostService{
    private final JsonParser jsonParser;
    private final String JSON_CATEGORY_ID_KEY = "category_id";
    private final String JSON_CUSTOM_CATEGORY_NAME_KEY = "category_name";
    private final String JSON_MEDIA_VIDEO_KEY = "video";
    private final String JSON_MEDIA_IMAGES_KEY = "images";
    private final String JSON_POST_ACTIVITY_COUNT_KEY = "count";

    @Autowired
    private PostRepository postRepository;

    public PostServiceImpl() {
        jsonParser = JsonParserFactory.getJsonParser();
    }

    @Override
    public ApiPost findPostById(String id, String loggedInUserId) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            return new ApiPost(post.getPostId(),
                    // TODO(sayoni): Instead of sending author id here, send author name
                    post.getAuthorId(),
                    post.getTitle(),
                    getCategoriesFromCategoryJson(post.getCategories(), JSON_CATEGORY_ID_KEY),
                    getCategoriesFromCategoryJson(post.getCustomCategories(), JSON_CUSTOM_CATEGORY_NAME_KEY),
                    getMediaVideoUrl(post.getMedia()),
                    getMediaImageUrls(post.getMedia()),
                    post.getDescription(),
                    getLikeCount(post.getLikes()),
                    post.getLastModifiedTimestamp(),
                    getUserLiked(loggedInUserId));
        }
        return null;
    }

    private List<String> getCategoriesFromCategoryJson(String categoryJson, String jsonKey) {
        Map<String, Object> parsedMap = this.jsonParser.parseMap(categoryJson);
        if (parsedMap.containsKey(jsonKey)) {
            // TODO(sayoni): Instead of sending category ids for key JSON_CATEGORY_ID_KEY, send category name
            return  (List<String>) parsedMap.get(jsonKey);
        }
        return new ArrayList<>();
    }

    private String getMediaVideoUrl(String mediaJson) {
        Map<String, Object> parsedMap = this.jsonParser.parseMap(mediaJson);
        if (parsedMap.containsKey(JSON_MEDIA_VIDEO_KEY)) {
            return (String) parsedMap.get(JSON_MEDIA_VIDEO_KEY);
        }
        return "";
    }

    private List<String> getMediaImageUrls(String mediaJson) {
        Map<String, Object> parsedMap = this.jsonParser.parseMap(mediaJson);
        if (parsedMap.containsKey(JSON_MEDIA_IMAGES_KEY)) {
            return (List<String>) parsedMap.get(JSON_MEDIA_IMAGES_KEY);
        }
        return new ArrayList<>();
    }

    private int getLikeCount(String likesJson) {
        Map<String, Object> parsedMap = this.jsonParser.parseMap(likesJson);
        if (parsedMap.containsKey(JSON_POST_ACTIVITY_COUNT_KEY)) {
            return (int) parsedMap.get(JSON_POST_ACTIVITY_COUNT_KEY);
        }
        return 0;
    }

    private boolean getUserLiked(String loggedInUserId) {
        if (loggedInUserId == null) {
            return false;
        }
        // TODO(sayoni): Check from the User table if the user has liked this post
        return loggedInUserId.equals("u1");
    }
}
