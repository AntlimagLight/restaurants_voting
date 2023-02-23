package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.dto.RestaurantOwnedMealDto;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends CrudRepository<Meal, Long> {

    Optional<Meal> findByRestaurantAndName(Restaurant restaurant, String name);

    Optional<Meal> findByRestaurantAndId(Restaurant restaurant, Long id);

    List<Meal> findAllByRestaurant(Restaurant restaurant);

    @Query(value = "SELECT r.id as restaurantId, m.id as id, m.name as name, m.cost as cost, " +
        "m.date as date, r.name as restaurantName FROM Restaurant r JOIN FETCH Meal m " +
        "ON r.id = m.restaurant.id WHERE m.date= :date")
    List<RestaurantOwnedMealDto> findAllByDateWithRestaurants(LocalDate date);

}
