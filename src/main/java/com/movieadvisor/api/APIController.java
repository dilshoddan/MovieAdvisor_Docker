package com.movieadvisor.api;

import com.movieadvisor.model.Comment;
import com.movieadvisor.model.Movie;
import com.movieadvisor.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.StringJoiner;

@RestController
public class APIController {
    private final MovieService movieService;

    @Autowired
    public APIController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(path = "/api/movies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping(path = "/api/movies/{id}")
    public Movie getMovie(@PathVariable("id") Long id) {
        return movieService.getMovie(id);
    }

    @PostMapping(path = "/api/movies")
    public Movie saveMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    @PostMapping(path = "/api/movie/{movieId}/user/{username}")
    public String vote(@PathVariable("movieId") Long movieId, @PathVariable("username") String username, @RequestBody Comment comment) {
        double rating = 0; String comm = null;
        if (comment.getVote() > 0)
            rating = movieService.voteForMovie(movieId, username, comment.getVote());
        if (comment.getComment() != null)
            comm = movieService.reviewMovie(movieId, username, comment.getComment());
        return new StringJoiner("\n")
                .add("New Review added:")
                .add("new raiting for the movie is " + rating)
                .add("Commented: " + comm)
                .toString();
    }

    @PutMapping(path = "/api/movies")
    public Movie updateMovie(@RequestBody Movie movie) {
        return movieService.updateMovie(movie);
    }

    @PatchMapping(path = "/api/movies")
    public Movie updateMovie2(@RequestBody Movie movie) {
        return movieService.updateMovie(movie);
    }

    @DeleteMapping(path = "/api/movies/{movieId}")
    public String deleteMovie(@PathVariable("movieId") Long movieId) {
        return movieService.deleteMovieById(movieId) ? "Success" : "Fail";
    }

}
