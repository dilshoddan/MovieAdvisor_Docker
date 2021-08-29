package com.movieadvisor.repository;

import com.movieadvisor.model.Star;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface StarRepository extends CrudRepository<Star, Long> {
    public abstract Optional<Star> findOneByNameIgnoreCase(String name);
}
