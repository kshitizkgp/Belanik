package com.backend.Belanik.post.service;

import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.auth.model.User;
import com.backend.Belanik.auth.repo.UserRepository;
import com.backend.Belanik.post.dto.*;
import com.backend.Belanik.post.model.Post;
import com.backend.Belanik.post.repo.PostRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{
    private final JsonParser jsonParser;
    private final String JSON_CATEGORY_ID_KEY = "category_id";
    private final String JSON_CUSTOM_CATEGORY_NAME_KEY = "category_name";
    private final String JSON_MEDIA_VIDEO_KEY = "video";
    private final String JSON_MEDIA_IMAGES_KEY = "images";
    private final String JSON_POST_ACTIVITY_COUNT_KEY = "count";

    private final String JSON_POST_ACTIVITY_USER_ACTIVITY_KEY = "user_activity";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public PostServiceImpl() {
        jsonParser = JsonParserFactory.getJsonParser();
    }

    @Override
    public ApiPost getPostById(String id, String loggedInUserId) {
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

    @Override
    public ApiPost createPost(ApiPost apiPost) {
        // TODO(sayoni): Check if the user has write access
        Post post = new Post();
        createPostUtil(post, apiPost);
        // TODO(sayoni): Set post author id with the user_id from the request
        post.setAuthorId("u1");
        post.setCreatedTimestamp(post.getLastModifiedTimestamp());

        post = postRepository.save(post);
        return createApiPostUtil(post, apiPost);
    }

    @Override
    public ApiPost updatePost(String id, ApiPost apiPost) {
        // TODO(sayoni): Check if the user has write access
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            createPostUtil(post, apiPost);
            postRepository.save(post);
            return createApiPostUtil(post, apiPost);
        }
        // TODO(sayoni): Throw exception that object to be updated does not exist
        return null;
    }

    @Override
    @Transactional
    public EngagePostResponse engagePost(LocalUser localUser, String post_id, EngagePostRequest request) {
        Optional<Post> postOptional = postRepository.findById(post_id);
        if(postOptional.isPresent()) {
            Post post = postOptional.get();
            int likeCount = getLikeCount(post.getLikes());
            int diff = request.getLikePost() ? 1 : (request.getUnlikePost() ? -1 : 0);
            User user = localUser.getUser();
            String newLikesJson = updateLikesJson(post.getLikes(), (likeCount + diff), user.getId(), diff);
            String newLikedPostJson = updateLikedPostJson(user, post.getPostId(), diff);
            user.setLikedPosts(newLikedPostJson);
            post.setLikes(newLikesJson);
            postRepository.save(post);
            userRepository.save(user);
            return new EngagePostResponse((long)getLikeCount(newLikesJson));
        }
        return new EngagePostResponse(-1L);
    }

    private void createPostUtil(Post post, ApiPost apiPost) {
        post.setTitle(apiPost.getTitle());
        post.setDescription(apiPost.getDescription());
        post.setCategories(createCategoryJsonFromCategories(apiPost.getCategoryNames(),
                JSON_CATEGORY_ID_KEY));
        post.setCustomCategories(createCategoryJsonFromCategories(apiPost.getCustomCategoryNames(),
                JSON_CUSTOM_CATEGORY_NAME_KEY));
        post.setMedia(createMediaJson(apiPost.getVideo(), apiPost.getImageUrls()));
        Date now = Calendar.getInstance().getTime();
        post.setLastModifiedTimestamp(now);
    }

    private ApiPost createApiPostUtil(Post post, ApiPost apiPost) {
        return new ApiPost(post.getPostId(),
                // TODO(sayoni): Send the author name instead of id
                post.getAuthorId(),
                post.getTitle(),
                apiPost.getCategoryNames(),
                apiPost.getCustomCategoryNames(),
                apiPost.getVideo(),
                apiPost.getImageUrls(),
                post.getDescription(),
                apiPost.getLikeCount(),
                post.getLastModifiedTimestamp(),
                false);
    }

    private List<String> getCategoriesFromCategoryJson(String categoryJson, String jsonKey) {
        Map<String, Object> parsedMap = this.jsonParser.parseMap(categoryJson);
        if (parsedMap.containsKey(jsonKey)) {
            // TODO(sayoni): Instead of sending category ids for key JSON_CATEGORY_ID_KEY, send category name
            return  (List<String>) parsedMap.get(jsonKey);
        }
        return new ArrayList<>();
    }

    private String createCategoryJsonFromCategories(List<String> categoryNames, String jsonKey) {
        JSONObject jsonObject = new JSONObject();
        if (categoryNames == null) {
            return jsonObject.toJSONString();
        }
        JSONArray jsonArray = new JSONArray();
        // TODO(sayoni): Convert each category name to category id
        jsonArray.addAll(categoryNames);
        jsonObject.put(jsonKey, jsonArray);
        return jsonObject.toJSONString();
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

    private String createMediaJson(String videoUrl, List<String> imageUrls) {
        JSONObject jsonObject = new JSONObject();
        // Media can contain either 1 video url or multiple image urls.
        // If both are present, video url will be given priority and stored.
        if (videoUrl != null) {
            jsonObject.put(JSON_MEDIA_VIDEO_KEY, videoUrl);
        } else if (imageUrls != null) {
            JSONArray imageJsonArray = new JSONArray();
            imageJsonArray.addAll(imageUrls);
            jsonObject.put(JSON_MEDIA_IMAGES_KEY, imageJsonArray);
        }
        return jsonObject.toJSONString();
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

    private String updateLikesJson(String likesJson, long newLikeCount, Long userId, int diff) {
        objectMapper.writerWithDefaultPrettyPrinter();
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        PostActivity newPostActivity = null;
        try {
            // TODO(kshitizkr): Fix this and use standard objectmapper.readValue method.
            JsonNode userActivityNode = objectMapper.readTree(likesJson).get(JSON_POST_ACTIVITY_USER_ACTIVITY_KEY);
            if(userActivityNode == null) {
                if(diff == 1) {
                    UserActivity activity = new UserActivity(Long.toString(userId), timestamp);
                    List<UserActivity> userActivities = new ArrayList<>();
                    userActivities.add(activity);
                    newPostActivity = new PostActivity(1, userActivities);
                    return objectMapper.writeValueAsString(newPostActivity);
                }
            }
            else {
                List<UserActivity> userActivities = new ArrayList<>(Arrays.asList(objectMapper.readValue(userActivityNode.toString(), UserActivity[].class)));
                userActivities.removeIf(x -> x.getUserId().equals(Long.toString(userId)));
                if(diff == 1) {
                    UserActivity newActivity = new UserActivity(Long.toString(userId), timestamp);
                    userActivities.add(newActivity);
                }
                newPostActivity = new PostActivity(userActivities.size(), userActivities);
                return objectMapper.writeValueAsString(newPostActivity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return likesJson;
    }

    private String updateLikedPostJson(User user, String postId, int diff){
        String likedPost = user.getLikedPosts();
        try {
            if(likedPost != null && likedPost != "") {
                Posts posts = objectMapper.readValue(likedPost, Posts.class);
                if(diff == 1)
                    posts.getPostId().add(postId);
                else
                    posts.getPostId().remove(postId);
                return objectMapper.writeValueAsString(posts);
            }
            else {
                if(diff == -1) {
                    return likedPost;
                }
                Set<String> postIds = new HashSet<>();
                postIds.add(postId);
                Posts posts = new Posts(postIds);
                return objectMapper.writeValueAsString(posts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return likedPost;
    }
}
