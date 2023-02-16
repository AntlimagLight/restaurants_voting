package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.dto.RestaurantWithMenuDto;
import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.MealRepository;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MealService {
    public static final String MEAL_ENTITY_NAME = "Meal";
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    @Caching(
            evict = {
                    @CacheEvict(value = "menu", key = "#restaurant_id"),
                    @CacheEvict(value = "dateMealCache", allEntries = true)
            }
    )
    @Transactional
    public Meal create(Meal meal, Long restaurant_id) throws AlreadyExistException {
        Restaurant restaurant = assertExistence(restaurantRepository.findById(restaurant_id), RESTAURANT_ENTITY_NAME);
        assertNotExistence(mealRepository.findByRestaurantAndName(restaurant, meal.getName()),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        meal.setRestaurant(restaurant);
        LocalDateTime now = LocalDateTime.now();
        meal.setDate(now.toLocalDate());
        return mealRepository.save(meal);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "menu", key = "#restaurant_id"),
                    @CacheEvict(value = "dateMealCache", allEntries = true)
            }
    )
    @Transactional
    public void update(Meal meal, Long id, Long restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantRepository.getById(restaurant_id);
        Meal oldMeal = assertExistence(mealRepository.findByRestaurantAndId(restaurant, id),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        meal.setId(id);
        meal.setRestaurant(restaurant);
        meal.setDate(oldMeal.getDate());
        mealRepository.save(meal);
    }

    public Meal getById(Long id, Long restaurant_id) throws NotExistException {
        return assertExistence(mealRepository.findByRestaurantAndId(restaurantRepository.getById(restaurant_id), id),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "menu", key = "#restaurant_id"),
                    @CacheEvict(value = "dateMealCache", allEntries = true)
            }
    )
    @Transactional
    public void delete(Long id, Long restaurant_id) throws NotExistException {
        assertExistence(mealRepository.findByRestaurantAndId(restaurantRepository.getById(restaurant_id), id),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        mealRepository.deleteById(id);
    }

    @Cacheable(value = "menu", key = "#restaurant_id")
    public List<Meal> getRestaurantMenu(Long restaurant_id) throws NotExistException {
        Restaurant restaurant = assertExistence(restaurantRepository.findById(restaurant_id), RESTAURANT_ENTITY_NAME);
        return mealRepository.findAllByRestaurant(restaurant);
    }

    @Cacheable(value = "dateMealCache", key = "#date")
    public List<RestaurantWithMenuDto> getAllByDateWithRestaurants(LocalDate date) {
        List<Meal> allMealList = mealRepository.findAllByDate(date);
        Map<Long, RestaurantWithMenuDto> dayMenu = new HashMap<>();
        for (Meal meal : allMealList) {
            List<Meal> menu;
            Long key = meal.getRestaurant().getId();
            if (dayMenu.containsKey(key)) {
                menu = dayMenu.get(key).getMenu();
            } else {
                log.trace("{} {} was found", RESTAURANT_ENTITY_NAME, key);
                menu = new ArrayList<>();
            }
            log.trace("add meal {} in {} {} ", meal.getName(), RESTAURANT_ENTITY_NAME, key);
            menu.add(meal);
            RestaurantWithMenuDto menuDto =
                    new RestaurantWithMenuDto(meal.getRestaurant().getId(), meal.getRestaurant().getName(), menu);
            dayMenu.put(key, menuDto);
        }
        return new ArrayList<>(dayMenu.values());
    }

}