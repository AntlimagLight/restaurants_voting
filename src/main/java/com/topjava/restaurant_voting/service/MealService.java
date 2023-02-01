package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.MealRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertExistence;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertNotExistence;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
@Slf4j
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
        LocalDateTime now = LocalDateTime.now();
        meal.setDate(now.toLocalDate());
        mealRepository.save(meal);
    }

    @Transactional
    public void update(Meal meal, Integer id, Integer restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantService.getById(restaurant_id);
        Meal oldMeal = assertExistence(mealRepository.findByRestaurantAndId(restaurant, id),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        meal.setId(id);
        meal.setRestaurant(restaurant);
        meal.setDate(oldMeal.getDate());
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

    @Cacheable(cacheNames = "dateMealCache", key = "#date")
    public Map<Integer, List<Meal>> getAllByDateWithRestaurants(LocalDate date) {
        List<Meal> allMealList = mealRepository.findAllByDate(date);
        Map<Integer, List<Meal>> result = new HashMap<>();
        for (Meal meal : allMealList) {
            Integer restaurant_id = meal.getRestaurant().getId();
            List<Meal> menu;
            if (result.containsKey(restaurant_id)) {
                menu = result.get(restaurant_id);
            } else {
                log.debug("{} {} was found", RESTAURANT_ENTITY_NAME, restaurant_id );
                menu = new ArrayList<>();
            }
            menu.add(meal);
            result.put(restaurant_id, menu);
        }
        return result;
    }

}