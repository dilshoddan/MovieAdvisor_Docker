package com.movieadvisor.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voteId;
    @NotNull
    private int noOfstars;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinTable(name = "MovieVote")
    private Movie movie;

    public Vote(int noOfstars, User user, Movie movie) {
        this.noOfstars = noOfstars;
        this.user = user;
        this.movie = movie;
        movie.getVotes().add(this);
    }
}
