package com.posterr_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Builder(builderMethodName = "userBuilder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usertable", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    @Size(max = 14)
    @Column(length = 14)
    private String username;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Post> posts;

}
