package com.posterr_backend.controllers;

import com.posterr_backend.dtos.PostRequest;
import com.posterr_backend.models.Post;
import com.posterr_backend.models.User;
import com.posterr_backend.services.PostService;
import com.posterr_backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public List<Post> getPosts(@RequestParam(required = false, defaultValue = "latest") String sort,
                               @RequestParam(required = false) String keyword) {
        return postService.getPosts(sort, keyword);
    }

    @PostMapping
    public ResponseEntity<Post> createOrRepost(
            @RequestParam Long userId,
            @RequestBody PostRequest request
    ) {
        User user = userService.getUser(userId);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Post post = postService.createOrRepost(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}
