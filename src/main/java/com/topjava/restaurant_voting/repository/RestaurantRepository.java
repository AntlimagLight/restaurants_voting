package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    Optional<Restaurant> findByName(String name);

    @Query("SELECT r FROM Restaurant r ORDER BY r.id ASC")
    List<Restaurant> findAll();

    Restaurant getById(Long id);
}
