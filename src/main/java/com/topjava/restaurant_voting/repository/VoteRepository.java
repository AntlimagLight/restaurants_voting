package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Integer> {

    Vote findByUserAndDate(User user, LocalDate date);

    List<Vote> findAllByDate(LocalDate date);

}
