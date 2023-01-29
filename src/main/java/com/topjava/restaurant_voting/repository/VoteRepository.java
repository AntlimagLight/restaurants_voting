package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Integer> {

    Optional<Vote> findByUserAndDate(User user, LocalDate date);

    List<Vote> findAllByUser(User user);

    List<Vote> findAllByDate(LocalDate date);

}
