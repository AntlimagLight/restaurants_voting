package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertExistence;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertNotExistence;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
@Transactional(readOnly = true)
public class MealService {
    public static final String MEAL_ENTITY_NAME = "Meal";
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private RestaurantService restaurantService;

    @Transactional
    public void create(Meal meal, Integer restaurant_id) throws AlreadyExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        assertNotExistence(mealRepository.findByRestaurantAndName(restaurant, meal.getName()),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        meal.setRestaurant(restaurant);
        mealRepository.save(meal);
    }

    @Transactional
    public void update(Meal meal, Integer id, Integer restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        assertExistence(mealRepository.findByRestaurantAndId(restaurant, id),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        meal.setId(id);
        meal.setRestaurant(restaurant);
        mealRepository.save(meal);
    }

    @Cacheable(cacheNames = "mealCache", key = "#id")
    public Meal getById(Integer id, Integer restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        return assertExistence(mealRepository.findByRestaurantAndId(restaurant, id),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
    }

    @Transactional
    public Integer delete(Integer id, Integer restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        assertExistence(mealRepository.findByRestaurantAndId(restaurant, id),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        mealRepository.deleteById(id);
        return id;
    }

    @Cacheable(cacheNames = "menuCache", key = "#restaurant_id")
    public List<Meal> getRestaurantMenu(Integer restaurant_id) {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        return mealRepository.findAllByRestaurant(restaurant);
    }
}