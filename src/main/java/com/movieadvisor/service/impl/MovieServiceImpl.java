package com.movieadvisor.service.impl;

import com.movieadvisor.exception.MovieNotFoundException;
import com.movieadvisor.exception.UserNotFoundException;
import com.movieadvisor.model.*;
import com.movieadvisor.repository.*;
import com.movieadvisor.model.*;
import com.movieadvisor.service.MovieService;
import com.movieadvisor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StarRepository starRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void setGenreRepository(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }
    public List<Movie> getAllMovies() {
        return (List<Movie>) movieRepository.findAll();
    }

    public Movie getMovie(Long id) {
        Optional<Movie> m = movieRepository.findById(id);
        return m.orElseThrow(MovieNotFoundException::new);
    }

    public Movie saveMovie(Movie movie) {
        List<Genre> genres = movie.getGenres();
        movie.setGenres(null);
        for (int i = 0; i < genres.size(); i++)
        {
            Optional<Genre> g = genreRepository.findOneByNameIgnoreCase(genres.get(i).getName());
            if (!g.isPresent()) {
                genres.set(i, genreRepository.save(new Genre(genres.get(i).getName())));
            } else {
                genres.set(i, g.get());
            }
        }
        movie.setGenres(genres);
        List<Star> stars = movie.getMovieStars();
        movie.setMovieStars(null);
        for (int i = 0; i < stars.size(); i++)
        {
            Optional<Star> s = starRepository.findOneByNameIgnoreCase(stars.get(i).getName());
            if (!s.isPresent()) {
                stars.set(i, starRepository.save(stars.get(i)));
            } else {
                stars.set(i, s.get());
            }
        }
        movie.setMovieStars(stars);
        return movieRepository.save(movie);
    }

    public double voteForMovie(Long movieId, String username, int rating) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Movie movie = getMovie(movieId);
        List<Vote> votes = voteRepository.findAllByMovie(movie);
        if (votes.stream().anyMatch(vote -> vote.getUser().getUsername().equals(username)))
            votes.stream().filter(vote -> vote.getUser().getUsername().equals(username))
                .forEach(vote -> vote.setNoOfstars(rating));
        else {
            Vote vote = new Vote(rating, user, movie);
            vote = voteRepository.save(vote);
            votes.add(vote);
        }
        double average = votes.stream().mapToInt(vote ->vote.getNoOfstars()).average().getAsDouble();
        movie.setRating(Math.round(10 * average)/10.0);
        movieRepository.save(movie);
        return movie.getRating();
    }

    @Override
    public String reviewMovie(Long movieId, String username, String comment) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Movie movie = getMovie(movieId);
        Review review = new Review(comment, user, movie);
        review = reviewRepository.save(review);
        return new StringJoiner("\n")
                .add("User commented:")
                .add(user.getUsername() + ": " + comment)
                .toString();
    }

    public Movie updateMovie(Movie theMovie) {
        Movie movie = getMovie(theMovie.getMovieId());
        if (theMovie.getTitle() != null) movie.setTitle(theMovie.getTitle());
        if (theMovie.getDescription() != null) movie.setDescription(theMovie.getDescription());
        if (theMovie.getImdbRate() > 0) movie.setImdbRate(theMovie.getImdbRate());
        if (theMovie.getImdbCount() > 0) movie.setImdbCount(theMovie.getImdbCount());
        if (theMovie.getGenres() != null && !theMovie.getGenres().isEmpty()) {
            movie.setGenres(theMovie.getGenres());
        }
        if (theMovie.getMovieStars() != null && !theMovie.getMovieStars().isEmpty()) {
            movie.setMovieStars(theMovie.getMovieStars());
        }
        if (theMovie.getReviews() != null && !theMovie.getReviews().isEmpty()) movie.setReviews(theMovie.getReviews());
        if (theMovie.getRating() > 0) movie.setRating(theMovie.getRating());
        return this.saveMovie(movie);
    }

    public boolean deleteMovieById(Long movieId) {
        movieRepository.deleteByMovieId(movieId);
        return true;
    }
/*
    public boolean deleteComment(Long id, String username) {
        Movie m = getMovie(id);
        if(m == null) return false;
        for(String user : m.getComments().keySet()) {
            if(user.equals(username)) {
                m.getComments().remove(user);
                movieRepository.save(m);
                return true;
            }
        }
        return false;
    }
*/
    public List<Movie> getAllNewMovies() {
        return getAllMovies().stream()
                .filter(Movie::isNew)
                .sorted((m1, m2) -> m2.getUpdatedAt().compareTo(m1.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    public List<Movie> getAllTopMovies() {
        return getAllMovies().stream().sorted(
                (o1, o2) -> (int) (100*(o2.getRating() - o1.getRating()))
        ).collect(Collectors.toList());
    }

    public List<Movie> getAllIMDbMovies() {
        return getAllMovies().stream().sorted(
                (o1, o2) -> (int) (100*(o2.getImdbRate() - o1.getImdbRate()))
        ).collect(Collectors.toList());
    }

    public List<Movie> getMoviesByGenre(String genre) {
        Genre g = genreRepository.findOneByNameIgnoreCase(genre).orElse(null);
        return getAllMovies().stream()
                .filter(
                        movie -> movie.getGenres().contains(g)
                ).collect(Collectors.toList());
    }

    @Override
    public List<String> getComments(Movie movie) {
        return movie
                .getReviews()
                .stream()
                .map(review -> review.getComment())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllGenres() {
        return ((List<Genre>)genreRepository.findAll()).stream().map(Genre::getName).collect(Collectors.toList());
    }
}
