package com.topjava.restaurant_voting.util;

import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class InitiateUtil implements CommandLineRunner {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public static final User user1 = new User(null,"Guest","guest@gmail.com",
            "{noop}guest",new Date(2022, Calendar.DECEMBER,10));

    public static final Restaurant rest1 = new Restaurant(null, "Tasty Island");
    public static final Restaurant rest2 = new Restaurant(null, "Burger Master");
    public static final Restaurant rest3 = new Restaurant(null, "Sushi city");

    @Override
    public void run(String... args) {
        restaurantRepository.save(rest1);
        restaurantRepository.save(rest2);
        restaurantRepository.save(rest3);
    }
}
