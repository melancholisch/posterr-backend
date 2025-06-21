package com.posterr_backend.controllers;

import com.posterr_backend.models.Post;
import com.posterr_backend.models.User;
import com.posterr_backend.services.PostService;
import com.posterr_backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDateTime;
import com.posterr_backend.repositories.PostRepository;
import com.posterr_backend.dtos.PostRequest;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostRepository postRepository;

    PostController(PostService postService, UserService userService, PostRepository postRepository) {
        this.postService = postService;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> getPosts(@RequestParam(required = false, defaultValue = "latest") String sort) {
        return postService.getPosts(sort);
    }

//    @PostMapping
//    public ResponseEntity<Post> createPost(@RequestBody String content, @RequestParam Long userId) {
//        User user = userService.getUser(userId);
//        LocalDateTime since = LocalDateTime.now().minusHours(24);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        long postsLast24h = postRepository.countByUserAndCreatedAtAfter(user, since);
//        if (postsLast24h >= 5) throw new IllegalArgumentException("Limite de 5 posts em 24h atingido.");
//
//        Post post = postService.createPost(user, content);
//        return ResponseEntity.status(HttpStatus.CREATED).body(post);
//    }

    @PostMapping
    public ResponseEntity<Post> createOrRepost(
            @RequestParam Long userId,
            @RequestBody PostRequest request
    ) {
        User user = userService.getUser(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        long postsLast24h = postRepository.countByUserAndCreatedAtAfter(user, since);
        if (postsLast24h >= 5) throw new IllegalArgumentException("Limite de 5 posts em 24h atingido.");

        Post post;
        if (request.getOriginalPostId() != null) {
            Post originalPost = postRepository.findById(request.getOriginalPostId()).orElse(null);
            if (originalPost == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            post = postService.repost(user, originalPost);
        } else {
            post = postService.createPost(user, request.getContent());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}
