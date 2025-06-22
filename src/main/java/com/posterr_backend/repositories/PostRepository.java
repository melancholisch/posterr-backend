package com.posterr_backend.repositories;


import com.posterr_backend.models.Post;
import com.posterr_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAll();
    List<Post> findAllByOrderByCreatedAtDesc();

    long countByUserAndCreatedAtAfter(User user, LocalDateTime after);
}
