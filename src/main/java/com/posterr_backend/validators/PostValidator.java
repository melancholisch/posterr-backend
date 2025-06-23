package com.posterr_backend.validators;

import com.posterr_backend.dtos.PostRequest;
import com.posterr_backend.handlers.MaxPostsPerDayException;
import com.posterr_backend.handlers.OwnPostException;
import com.posterr_backend.handlers.RepostWithContentException;
import com.posterr_backend.models.Post;
import com.posterr_backend.models.User;
import com.posterr_backend.repositories.PostRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostValidator {

    private final PostRepository postRepository;

    public PostValidator(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void validateMaxPostsPerDay(User user) {
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        long postsLast24h = postRepository.countByUserAndCreatedAtAfter(user, since);
        if (postsLast24h >= 5) throw new MaxPostsPerDayException("Only five posts per day.");
    }

    public void validateRepost(User user, Post originalPost, PostRequest request) {
        if (originalPost.getUser().getId().equals(user.getId())) {
            throw new OwnPostException("You cannot repost your own post.");
        }
        if (request.getContent() != null && !request.getContent().isBlank()) {
            throw new RepostWithContentException("Reposts cannot have content.");
        }
        if (originalPost.isRepost()) {
            throw new IllegalArgumentException("Reposts cannot be reposted.");
        }
        long repostCount = postRepository.countRepostsByUserAndOriginalPost(user, originalPost);
        if (repostCount > 0) {
            throw new IllegalArgumentException("You have already reposted this post.");
        }
    }
}