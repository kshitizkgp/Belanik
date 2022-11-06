package com.backend.Belanik.post.service;

import com.backend.Belanik.auth.dto.LocalUser;
import com.backend.Belanik.auth.model.User;
import com.backend.Belanik.auth.repo.UserRepository;
import com.backend.Belanik.post.dto.*;
import com.backend.Belanik.post.model.Category;
import com.backend.Belanik.post.model.Post;
import com.backend.Belanik.post.repo.CategoryRepository;
import com.backend.Belanik.post.repo.PostRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{
    private final String JSON_POST_ACTIVITY_USER_ACTIVITY_KEY = "user_activity";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ApiPost getPostById(String id, LocalUser currentUser) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            return new ApiPost(post.getPostId(),
                    getUserNameFromId(Long.parseLong(post.getAuthorId())),
                    post.getTitle(),
                    getCategoryNamesFromCategoryJson(post.getCategories()),
                    getCustomCategoriesFromCategoryJson(post.getCustomCategories()),
                    getMediaVideoUrl(post.getMedia()),
                    getMediaImageUrls(post.getMedia()),
                    post.getDescription(),
                    getLikeCount(post.getLikes()),
                    post.getLastModifiedTimestamp(),
                    // TODO(sayoni): getUserLiked(loggedInUserId)
                    false
                    );
        }
        return null;
    }

    @Override
    public ApiPost createPost(ApiPost apiPost, LocalUser currentUser) {
        Post post = new Post();
        createPostUtil(post, apiPost);
        User user = currentUser.getUser();
        post.setAuthorId(String.valueOf(user.getId()));
        post.setCreatedTimestamp(post.getLastModifiedTimestamp());

        post = postRepository.save(post);
        return createApiPostUtil(post, apiPost, user);
    }

    @Override
    public ApiPost updatePost(String id, ApiPost apiPost) {
        // TODO(sayoni): Check if the user has write access
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            createPostUtil(post, apiPost);
            postRepository.save(post);
            return createApiPostUtil(post, apiPost, null);
        }
        // TODO(sayoni): Throw error that object to be updated does not exist
        return null;
    }

    @Override
    @Transactional
    public EngagePostResponse engagePost(LocalUser localUser, String post_id, EngagePostRequest request) {
        Optional<Post> postOptional = postRepository.findById(post_id);
        if(postOptional.isPresent()) {
            Post post = postOptional.get();
            int diff = request.getLikePost() ? 1 : (request.getUnlikePost() ? -1 : 0);
            User user = localUser.getUser();
            String newLikesJson = updateLikesJson(post.getLikes(), user.getId(), diff);
            String newLikedPostJson = updateLikedPostJson(user, post.getPostId(), diff);
            user.setLikedPosts(newLikedPostJson);
            post.setLikes(newLikesJson);
            postRepository.save(post);
            userRepository.save(user);
            return new EngagePostResponse(getLikeCount(newLikesJson));
        }
        return new EngagePostResponse(-1L);
    }

    private void createPostUtil(Post post, ApiPost apiPost) {
        post.setTitle(apiPost.getTitle());
        post.setLastModifiedTimestamp(Calendar.getInstance().getTime());

        // Create Categories JSON
        if (apiPost.getCategoryNames() != null) {
            List<String> categoryIdsList = new ArrayList<>();
            for (String categoryName : apiPost.getCategoryNames()) {
                String categoryId = getCategoryIdFromName(categoryName);
                if (!categoryId.isEmpty()) {
                    categoryIdsList.add(categoryId);
                }
            }
            CategoryIds categoryIds = new CategoryIds();
            categoryIds.setCategoryIds(categoryIdsList);
            try {
                String categoryJson = objectMapper.writeValueAsString(categoryIds);
                post.setCategories(categoryJson);
            } catch (IOException exception) {
                System.out.println("Error encountered while creating Categories Json");
                exception.printStackTrace();
            }
        }

        // Create CustomCategories JSON
        if (apiPost.getCustomCategoryNames() != null) {
            CustomCategories customCategories = new CustomCategories();
            customCategories.setCategoryNames(apiPost.getCustomCategoryNames());
            try {
                String customCategoriesJson = objectMapper.writeValueAsString(customCategories);
                post.setCustomCategories(customCategoriesJson);
            } catch (IOException exception) {
                System.out.println("Error encountered while creating CustomCategories Json");
                exception.printStackTrace();
            }
        }

        // Create Media JSON
        Media media = new Media();
        // Only one of video or imageUrls can be set
        if (apiPost.getVideo() != null && !apiPost.getVideo().isEmpty()) {
            media.setVideo(apiPost.getVideo());
        } else if (apiPost.getImageUrls() != null && !apiPost.getImageUrls().isEmpty()) {
            media.setImages(apiPost.getImageUrls());
        }
        try {
            String mediaJson = objectMapper.writeValueAsString(media);
            post.setMedia(mediaJson);
        } catch (IOException exception) {
            System.out.println("Error encountered while creating Media Json");
            exception.printStackTrace();
        }

        post.setDescription(apiPost.getDescription());
    }

    private ApiPost createApiPostUtil(Post post, ApiPost apiPost, User user) {
        return new ApiPost(post.getPostId(),
                user.getDisplayName(),
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

    private List<String> getCategoryNamesFromCategoryJson(String categoryJson) {
        List<String> categoryNames = new ArrayList<>();
        try {
            if (categoryJson != null && !categoryJson.isEmpty()) {
                CategoryIds categoryIds = objectMapper.readValue(categoryJson, CategoryIds.class);
                List<String> categoryIdsList = categoryIds.getCategoryIds();
                for (String categoryId: categoryIdsList) {
                    categoryNames.add(getCategoryNameFromId(categoryId));
                }
            }
        } catch (IOException exception) {
            System.out.println("Error encountered while parsing Categories Json");
            exception.printStackTrace();
        }
        return categoryNames;
    }

    private List<String> getCustomCategoriesFromCategoryJson(String customCategoryJson) {
        try {
            if (customCategoryJson != null && !customCategoryJson.isEmpty()) {
                CustomCategories customCategories = objectMapper.readValue(customCategoryJson, CustomCategories.class);
                return customCategories.getCategoryNames();
            }
        } catch (IOException exception) {
            System.out.println("Error encountered while parsing CustomCategories Json");
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private String getMediaVideoUrl(String mediaJson) {
        try {
            if (mediaJson != null && !mediaJson.isEmpty()) {
                Media media = objectMapper.readValue(mediaJson, Media.class);
                return media.getVideo();
            }
        } catch (IOException exception) {
            System.out.println("Error encountered while parsing Media Json");
            exception.printStackTrace();
        }
        return "";
    }

    private List<String> getMediaImageUrls(String mediaJson) {
        try {
            if (mediaJson != null && !mediaJson.isEmpty()) {
                Media media = objectMapper.readValue(mediaJson, Media.class);
                return media.getImages();
            }
        } catch (IOException exception) {
            System.out.println("Error encountered while parsing Media Json");
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private long getLikeCount(String likesJson) {
        try {
            if (likesJson != null && !likesJson.isEmpty()) {
                PostActivity postActivity = objectMapper.readValue(likesJson, PostActivity.class);
                return postActivity.getCount();
            }
        } catch (IOException exception) {
            System.out.println("Error encountered while parsing PostActivity Json");
            exception.printStackTrace();
        }
        return 0L;
    }

    private String updateLikesJson(String likesJson, Long userId, int diff) {
        objectMapper.writerWithDefaultPrettyPrinter();
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        PostActivity newPostActivity;
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
                List<UserActivity> userActivities = new ArrayList<>(Arrays.asList(
                        objectMapper.readValue(userActivityNode.toString(), UserActivity[].class)));
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
            if(likedPost != null && !likedPost.equals("")) {
                PostIds postIds = objectMapper.readValue(likedPost, PostIds.class);
                if(diff == 1)
                    postIds.getPostId().add(postId);
                else
                    postIds.getPostId().remove(postId);
                return objectMapper.writeValueAsString(postIds);
            }
            else {
                if(diff == -1) {
                    return likedPost;
                }
                Set<String> postIds = new HashSet<>();
                postIds.add(postId);
                PostIds posts = new PostIds(postIds);
                return objectMapper.writeValueAsString(posts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return likedPost;
    }

    private String getUserNameFromId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getDisplayName();
        }
        return "";
    }

    private String getCategoryNameFromId(String categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get().getName();
        }
        return "";
    }

    private String getCategoryIdFromName(String categoryName) {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get().getCategoryId();
        }
        return "";
    }
}
