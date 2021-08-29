package com.movieadvisor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Data
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "MovieGenre")
    private List<Genre> genres = new ArrayList<>();
    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    private List<Vote> votes = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "MovieStar")
    private List<Star> movieStars = new ArrayList<>();
    private double rating;
    private String movieImgURL;
    private double imdbRate;
    private long imdbCount;
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Movie(Long movieId, String title, String description) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
    }

    public Movie(String title, String description, List<Genre> genres, List<Star> stars, double rating, String movieImgURL, double imdbRate, int imdbCount) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.movieStars = stars;
        this.rating = rating;
        this.movieImgURL = movieImgURL;
        this.imdbRate = imdbRate;
        this.imdbCount = imdbCount;
    }

    public boolean isNew() {
        return LocalDateTime.now().isBefore(updatedAt.plusDays(7));
    }
    public String getGenresString() {
        StringJoiner sj = new StringJoiner(", ");
        for (Genre genre : genres)
            sj.add(genre.getName());
        return sj.toString();
    }

    public long getNoVotes() {
        return votes.size();
    }

    public String getStarsString() {
        StringJoiner sj = new StringJoiner(", ");
        for (Star star : movieStars)
            sj.add(star.getName());
        return sj.toString();
    }
}

