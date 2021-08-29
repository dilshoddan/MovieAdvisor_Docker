package com.movieadvisor.service;


import com.movieadvisor.model.Movie;
import com.movieadvisor.repository.GenreRepository;

import java.util.List;

public interface MovieService {
    public void setGenreRepository(GenreRepository genreRepository);
    public List<Movie> getAllMovies();
    public Movie getMovie(Long id);
    public Movie saveMovie(Movie movie);
    public double voteForMovie(Long movieId, String username, int rating);
    public String reviewMovie(Long movieId, String username, String comment);
    public Movie updateMovie(Movie movie);
    public boolean deleteMovieById(Long movieId);
    public List<Movie> getAllTopMovies();
    public List<Movie> getAllNewMovies();
    public List<Movie> getAllIMDbMovies();
    public List<Movie> getMoviesByGenre(String genre);
    public List<String> getComments(Movie movie);
    public List<String> getAllGenres();
}
