package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {
    Restaurant findByName (String name);
}
