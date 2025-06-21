package com.posterr_backend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JsonManagedReference
    private User user;

    @Column(nullable = false, length = 777)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRepost = false;

    @ManyToOne
    private Post originalPost;

}
