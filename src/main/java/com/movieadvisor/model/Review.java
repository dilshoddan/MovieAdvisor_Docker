package com.movieadvisor.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;
    private String comment;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinTable(name = "MovieReview")
    private Movie movie;
    private LocalDateTime commentedAt;

    public Review(String comment, User user, Movie movie) {
        this.comment = comment;
        this.user = user;
        this.movie = movie;
        commentedAt = LocalDateTime.now();
        movie.getReviews().add(this);
    }
}
