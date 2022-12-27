package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant createRestaurant(Restaurant restaurant) throws AlreadyExistException {
        if(restaurantRepository.findByName(restaurant.getName()) != null) {
            throw new AlreadyExistException("Ресторан с таким именем уже существует");
        }
        return restaurantRepository.save(restaurant);
    }

}