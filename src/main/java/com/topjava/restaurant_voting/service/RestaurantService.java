package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.topjava.restaurant_voting.util.ValidationUtils.assertExistence;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertNotExistence;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {
    public static final String RESTAURANT_ENTITY_NAME = "Restaurant";
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void create(Restaurant restaurant) throws AlreadyExistException {
        assertNotExistence(restaurantRepository.findByName(restaurant.getName()),
                RESTAURANT_ENTITY_NAME + " with this name");
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void update(Integer id, Restaurant restaurant) throws NotExistException {
        assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME);
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }

    @Cacheable(cacheNames = "restaurantCache", key = "#id")
    public Restaurant getById(Integer id) throws NotExistException {
        return assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME);
    }

    @Transactional
    public Integer delete(int id) throws NotExistException {
        assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME);
        restaurantRepository.deleteById(id);
        return id;
    }

    @Cacheable(cacheNames = "restaurantList")
    public List<Restaurant> getAll() {
        return (List<Restaurant>) restaurantRepository.findAll();
    }

}