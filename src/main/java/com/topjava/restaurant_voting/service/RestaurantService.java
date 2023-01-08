package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
@Transactional(readOnly = true)
public class RestaurantService {
    public static final String RESTAURANT_ENTITY_NAME = "Restaurant";
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    public void create(Restaurant restaurant) throws AlreadyExistException {
        if (restaurantRepository.findByName(restaurant.getName()) != null) {
            throw new AlreadyExistException(RESTAURANT_ENTITY_NAME);
        }
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void update(Integer id, Restaurant restaurant) throws NotExistException {
        if (restaurantRepository.findById(id).isEmpty()) {
            throw new NotExistException(RESTAURANT_ENTITY_NAME);
        }
        restaurant.setId(id);
        restaurantRepository.save(restaurant);
    }

    public Restaurant getById(Integer id) throws NotExistException {
        Optional<Restaurant> opt = restaurantRepository.findById(id);
        if (opt.isEmpty()) {
            throw new NotExistException(RESTAURANT_ENTITY_NAME);
        }
        return opt.get();
    }

    @Transactional
    public Integer delete(int id) throws NotExistException {
        if (restaurantRepository.findById(id).isEmpty()) {
            throw new NotExistException(RESTAURANT_ENTITY_NAME);
        }
        restaurantRepository.deleteById(id);
        return id;
    }

    public List<Restaurant> getAll() {
        return (List<Restaurant>) restaurantRepository.findAll();
    }

}