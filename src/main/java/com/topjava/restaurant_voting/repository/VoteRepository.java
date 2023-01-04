package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Integer> {

    Vote findByUser(User user);

}
