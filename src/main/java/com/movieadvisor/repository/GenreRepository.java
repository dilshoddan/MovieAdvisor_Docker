package com.movieadvisor.repository;

import com.movieadvisor.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface GenreRepository extends CrudRepository<Genre, Integer> {
    public abstract Optional<Genre> findOneByNameIgnoreCase(String name);
}
