package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
