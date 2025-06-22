package com.posterr_backend.services;

import com.posterr_backend.dtos.PostRequest;
import com.posterr_backend.handlers.MaxPostsPerDayException;
import com.posterr_backend.handlers.RepostWithContentException;
import com.posterr_backend.handlers.OwnPostException;
import com.posterr_backend.models.Post;
import com.posterr_backend.models.User;
import com.posterr_backend.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts(String sort) {
        if ("latest".equalsIgnoreCase(sort)) {
            return postRepository.findAllByOrderByCreatedAtDesc();
        }

        return postRepository.findAll();
    }

    public Post createPost(User user, String content) {
        if (content.length() > 777) throw new IllegalArgumentException("Max 777 caracteres.");

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Post repost(User user, Post originalPost) {
        Post repost = new Post();
        repost.setUser(user);
        repost.setContent(originalPost.getContent());
        repost.setCreatedAt(LocalDateTime.now());
        repost.setRepost(true);
        repost.setOriginalPost(originalPost);
        return postRepository.save(repost);
    }

    public Post createOrRepost(User user, PostRequest request) {
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        long postsLast24h = postRepository.countByUserAndCreatedAtAfter(user, since);
        if (postsLast24h >= 5) throw new MaxPostsPerDayException("Just five posts per day.");

        if (request.getOriginalPostId() != null) {
            Post originalPost = postRepository.findById(request.getOriginalPostId())
                    .orElseThrow(() -> new IllegalArgumentException("Original post not found."));
            if (originalPost.getUser().getId().equals(user.getId())) {
                throw new OwnPostException("You can't repost your own post.");
            }
            if (request.getContent() != null && !request.getContent().isBlank()) {
                throw new RepostWithContentException("Repost with content not allowed.");
            }
            return repost(user, originalPost);
        } else {
            return createPost(user, request.getContent());
        }
    }

}
