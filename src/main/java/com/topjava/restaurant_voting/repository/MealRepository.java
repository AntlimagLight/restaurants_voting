package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MealRepository extends CrudRepository<Meal, Integer> {

    Meal findByRestaurantAndName(Restaurant restaurant, String name);

    Meal findByRestaurantAndId(Restaurant restaurant, Integer id);

    List<Meal> findAllByRestaurant(Restaurant restaurant);


}
