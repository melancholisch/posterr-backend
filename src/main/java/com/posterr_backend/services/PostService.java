package com.posterr_backend.services;

import com.posterr_backend.dtos.PostRequest;
import com.posterr_backend.models.Post;
import com.posterr_backend.models.User;
import com.posterr_backend.repositories.PostRepository;
import org.springframework.stereotype.Service;
import com.posterr_backend.validators.PostValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostValidator postValidator;

    public PostService(PostRepository postRepository, PostValidator postValidator) {
        this.postRepository = postRepository;
        this.postValidator = postValidator;
    }

    public List<Post> getPosts(String sort, String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            if ("trending".equalsIgnoreCase(sort)) {
                return postRepository.findOriginalPostsByContentContainingOrderByRepostsDesc(keyword);
            }
            return postRepository.findOriginalPostsByContentContainingOrderByCreatedAtDesc(keyword);
        }
        if ("trending".equalsIgnoreCase(sort)) {
            return postRepository.findOriginalPostsOrderByRepostsDesc();
        }
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post createOrRepost(User user, PostRequest request) {
        postValidator.validateMaxPostsPerDay(user);

        if (request.getOriginalPostId() != null) {
            Post originalPost = postRepository.findById(request.getOriginalPostId())
                    .orElseThrow(() -> new IllegalArgumentException("Original post not found."));
            postValidator.validateRepost(user, originalPost, request);
            return createRepost(user, originalPost);
        }
        return createPost(user, request.getContent());
    }

    private Post createPost(User user, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be empty.");
        }
        if (content.length() > 777) {
            throw new IllegalArgumentException("Content exceeds maximum length of 777 characters.");
        }
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    private Post createRepost(User user, Post originalPost) {
        Post repost = new Post();
        repost.setUser(user);
        repost.setContent(originalPost.getContent());
        repost.setCreatedAt(LocalDateTime.now());
        repost.setRepost(true);
        repost.setOriginalPost(originalPost);
        return postRepository.save(repost);
    }

}
