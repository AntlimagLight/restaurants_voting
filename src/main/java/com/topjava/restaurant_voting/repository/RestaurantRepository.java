package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {

    Optional<Restaurant> findByName(String name);
    @Query("select r from Restaurant r order by r.id asc")
    List<Restaurant> findAll();
}
