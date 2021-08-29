package com.movieadvisor.controller;

import com.movieadvisor.model.Comment;
import com.movieadvisor.model.Movie;
import com.movieadvisor.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("fiveNewMovies",
                movieService.getAllNewMovies().stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("fiveTopMovies",
                movieService.getAllTopMovies().stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("greeting", "Hello");
        return "index";
    }

    @GetMapping("newmovies")
    public String newMovies(Model model) {
        model.addAttribute("page", "newmovies");
        model.addAttribute("movies", movieService.getAllNewMovies());
        return "movies";
    }

    @GetMapping("topmovies")
    public String topMovies(Model model) {
        model.addAttribute("page", "topmovies");
        model.addAttribute("movies", movieService.getAllTopMovies());
        return "movies";
    }

    @GetMapping("imdbmovies")
    public String imdbMovies(Model model) {
        model.addAttribute("page", "imdbmovies");
        model.addAttribute("movies", movieService.getAllIMDbMovies());
        return "movies";
    }

    @GetMapping("genres")
    public String genres() {
        return "redirect:genres/Drama";
    }

    @GetMapping("genres/{genre}")
    public String genre(@PathVariable("genre") String genre, Model model) {
        model.addAttribute("genres", movieService.getAllGenres());
        model.addAttribute("movies",
                movieService.getMoviesByGenre(genre));
        model.addAttribute("page", genre);
        return "genres";
    }

    @GetMapping("about")
    public String about() {
        return "about";
    }

    @GetMapping("movie/{id}")
    public String movie(@PathVariable("id") Long id, @RequestParam(value = "rate", required = false) Integer rate, Model model, HttpSession session) {
        String username = (String)session.getAttribute("username");
        model.addAttribute("username", username);
        Comment comment = new Comment();
        comment.setVote(rate == null ? 0 : rate.intValue());
        if(username != null) model.addAttribute("comment", comment);
        Movie movie = movieService.getMovie(id);
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", movie.getReviews());
        model.addAttribute("rate", rate == null ? 0 : rate.intValue());
        return "movie";
    }

    @PostMapping("movie/{id}")
    public String addComment(@PathVariable("id") Long movieId, @ModelAttribute Comment comment, Model model, HttpSession session) {
        String username = (String)session.getAttribute("username");
        double rating = 0; String comm = null;
        if (comment.getVote() > 0)
            rating = movieService.voteForMovie(movieId, username, comment.getVote());
        if (comment.getComment() != null)
            comm = movieService.reviewMovie(movieId, username, comment.getComment());
        return "redirect:/movie/"+ movieId.toString()+"?rate="+comment.getVote();
    }
}
