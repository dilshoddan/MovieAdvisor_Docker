package com.movieadvisor.repository;

import com.movieadvisor.model.Movie;
import com.movieadvisor.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface VoteRepository extends CrudRepository<Vote, Long> {
    public abstract List<Vote> findAllByMovie(Movie movie);
}
