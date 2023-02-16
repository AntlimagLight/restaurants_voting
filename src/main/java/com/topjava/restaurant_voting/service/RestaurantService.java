package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.topjava.restaurant_voting.util.ValidationUtils.assertExistence;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertNotExistence;

@Service
@Slf4j
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "restaurant")
@RequiredArgsConstructor
public class RestaurantService {
    public static final String RESTAURANT_ENTITY_NAME = "Restaurant";
    private final RestaurantRepository restaurantRepository;

    @Transactional
    @CacheEvict(value = "restaurantList", key = "0")
    public Restaurant create(Restaurant restaurant) throws AlreadyExistException {
        assertNotExistence(restaurantRepository.findByName(restaurant.getName()),
                RESTAURANT_ENTITY_NAME + " with this name");
        return restaurantRepository.save(restaurant);
    }

    @Cacheable
    public Restaurant getById(Long id) throws NotExistException {
        return assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "restaurantList", key = "0"),
                    @CacheEvict
            }
    )
    public void delete(long id) throws NotExistException {
        assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME);
        restaurantRepository.deleteById(id);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(key = "#id"),
                    @CacheEvict(value = "restaurantList", key = "0")
            }
    )
    public void update(Long id, Restaurant restaurant) throws NotExistException {
        assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME);
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }

    @Cacheable(value = "restaurantList", key = "0")
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

}