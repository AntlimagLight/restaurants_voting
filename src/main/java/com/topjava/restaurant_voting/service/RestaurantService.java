package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.dto.RestaurantDto;
import com.topjava.restaurant_voting.mapper.RestaurantMapper;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class RestaurantService {
    public static final String RESTAURANT_ENTITY_NAME = "Restaurant";
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    @CacheEvict(value = "restaurantList", key = "0")
    public Restaurant create(RestaurantDto restaurant) {
        assertNotExistence(restaurantRepository.findByName(restaurant.getName()),
                RESTAURANT_ENTITY_NAME + " with this name");
        return restaurantRepository.save(restaurantMapper.toModel(restaurant));
    }

    @Cacheable(value = "restaurant", key = "#id")
    public RestaurantDto getById(Long id) {
        return restaurantMapper.toDTO(assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME));
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "restaurantList", key = "0"),
                    @CacheEvict(value = "dateMealCache", allEntries = true),
                    @CacheEvict(value = "restaurant", key = "#id")
            }
    )
    public void delete(long id) {
        assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME);
        restaurantRepository.deleteById(id);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "restaurant", key = "#id"),
                    @CacheEvict(value = "restaurantList", key = "0"),
                    @CacheEvict(value = "dateMealCache", allEntries = true)
            }
    )
    public void update(Long id, RestaurantDto restaurant) {
        assertExistence(restaurantRepository.findById(id), RESTAURANT_ENTITY_NAME);
        restaurant.setId(id);
        restaurantRepository.save(restaurantMapper.toModel(restaurant));
    }

    @Cacheable(value = "restaurantList", key = "0")
    public List<RestaurantDto> getAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toDTO)
                .toList();
    }

}