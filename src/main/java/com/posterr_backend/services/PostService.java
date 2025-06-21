package com.posterr_backend.services;

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

}
