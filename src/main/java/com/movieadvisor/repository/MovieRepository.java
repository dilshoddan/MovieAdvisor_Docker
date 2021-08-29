package com.movieadvisor.repository;

import com.movieadvisor.model.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MovieRepository extends CrudRepository<Movie, Long> {
    public abstract void deleteByMovieId(long movieId);
}
