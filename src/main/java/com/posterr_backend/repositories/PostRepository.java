package com.posterr_backend.repositories;


import com.posterr_backend.models.Post;
import com.posterr_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAll();
    List<Post> findAllByOrderByCreatedAtDesc();

    long countByUserAndCreatedAtAfter(User user, LocalDateTime after);

    @Query("SELECT p FROM Post p WHERE p.isRepost = false ORDER BY (SELECT COUNT(r) FROM Post r WHERE r.originalPost = p) DESC, p.createdAt DESC")
    List<Post> findOriginalPostsOrderByRepostsDesc();

    @Query("SELECT p FROM Post p WHERE p.isRepost = false AND p.content LIKE %:keyword% ORDER BY p.createdAt DESC")
    List<Post> findOriginalPostsByContentContainingOrderByCreatedAtDesc(String keyword);

    @Query("SELECT p FROM Post p WHERE p.isRepost = false AND p.content LIKE %:keyword% ORDER BY (SELECT COUNT(r) FROM Post r WHERE r.originalPost = p) DESC, p.createdAt DESC")
    List<Post> findOriginalPostsByContentContainingOrderByRepostsDesc(String keyword);
}
