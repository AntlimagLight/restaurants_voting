package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.dto.MealDto;
import com.topjava.restaurant_voting.dto.RestaurantWithMenuDto;
import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.mapper.MealMapper;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.MealRepository;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
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
    public static final MealMapper mealMapper = Mappers.getMapper(MealMapper.class);

    @Caching(
            evict = {
                    @CacheEvict(value = "menu", key = "#restaurant_id"),
                    @CacheEvict(value = "dateMealCache", allEntries = true)
            }
    )
    @Transactional
    public Meal create(MealDto meal, Long restaurant_id) throws AlreadyExistException {
        Restaurant restaurant = assertExistence(restaurantRepository.findById(restaurant_id), RESTAURANT_ENTITY_NAME);
        assertNotExistence(mealRepository.findByRestaurantAndName(restaurant, meal.getName()),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        Meal entity = mealMapper.toModel(meal);
        entity.setRestaurant(restaurant);
        entity.setDate(LocalDateTime.now().toLocalDate());
        return mealRepository.save(entity);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "menu", key = "#restaurant_id"),
                    @CacheEvict(value = "dateMealCache", allEntries = true)
            }
    )
    @Transactional
    public void update(MealDto meal, Long id, Long restaurant_id) throws NotExistException {
        Restaurant restaurant = restaurantRepository.getById(restaurant_id);
        Meal oldMeal = assertExistence(mealRepository.findByRestaurantAndId(restaurant, id),
                "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME);
        Meal entity = mealMapper.toModel(meal);
        entity.setId(id);
        entity.setRestaurant(restaurant);
        entity.setDate(oldMeal.getDate());
        mealRepository.save(entity);
    }

    public MealDto getById(Long id, Long restaurant_id) throws NotExistException {
        return mealMapper
                .toDTO(assertExistence(mealRepository
                                .findByRestaurantAndId(restaurantRepository
                                        .getById(restaurant_id), id),
                        "In this " + RESTAURANT_ENTITY_NAME + " the specified " + MEAL_ENTITY_NAME));
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
    public List<MealDto> getRestaurantMenu(Long restaurant_id) throws NotExistException {
        Restaurant restaurant = assertExistence(restaurantRepository.findById(restaurant_id), RESTAURANT_ENTITY_NAME);
        return mealRepository.findAllByRestaurant(restaurant).stream().map(mealMapper::toDTO).toList();
    }

    @Cacheable(value = "dateMealCache", key = "#date")
    public List<RestaurantWithMenuDto> getAllByDateWithRestaurants(LocalDate date) {
        List<Meal> allMealList = mealRepository.findAllByDate(date);
        Map<Long, RestaurantWithMenuDto> dayMenu = new HashMap<>();
        for (Meal meal : allMealList) {
            List<MealDto> menu;
            Long key = meal.getRestaurant().getId();
            if (dayMenu.containsKey(key)) {
                menu = dayMenu.get(key).getMenu();
            } else {
                log.trace("{} {} was found", RESTAURANT_ENTITY_NAME, key);
                menu = new ArrayList<>();
            }
            log.trace("add meal {} in {} {} ", meal.getName(), RESTAURANT_ENTITY_NAME, key);
            menu.add(mealMapper.toDTO(meal));
            RestaurantWithMenuDto menuDto =
                    new RestaurantWithMenuDto(meal.getRestaurant().getId(), meal.getRestaurant().getName(), menu);
            dayMenu.put(key, menuDto);
        }
        return new ArrayList<>(dayMenu.values());
    }

}