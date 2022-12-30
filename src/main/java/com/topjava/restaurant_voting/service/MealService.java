package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.MealRepository;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;

@Service
public class MealService {
    public static final String MEAL_ENTITY_NAME = "Meal";
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private RestaurantService restaurantService;

    public void create(Meal meal, Integer restaurant_id) throws AlreadyExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        if (mealRepository.findByRestaurantAndName(restaurant, meal.getName()) != null) {
            throw new AlreadyExistException("In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        }
        meal.setRestaurant(restaurant);
        mealRepository.save(meal);
    }

    public void update(Meal meal, Integer id, Integer restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        if (mealRepository.findByRestaurantAndId(restaurant, id) == null) {
            throw new NotExistException("In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        }
        meal.setId(id);
        meal.setRestaurant(restaurant);
        mealRepository.save(meal);
    }

    public Meal getById(Integer id, Integer restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        Meal meal = mealRepository.findByRestaurantAndId(restaurant, id);
        if (meal == null) {
            throw new NotExistException("In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        }
        return meal;
    }

    public Integer delete(Integer id, Integer restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        if (mealRepository.findByRestaurantAndId(restaurant, id) == null) {
            throw new NotExistException("In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        }
        mealRepository.deleteById(id);
        return id;
    }

    public List<Meal> getRestaurantMenu(Integer restaurant_id) {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        return mealRepository.findAllByRestaurant(restaurant);
    }
}