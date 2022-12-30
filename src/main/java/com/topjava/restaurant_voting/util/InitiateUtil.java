package com.topjava.restaurant_voting.util;

import com.topjava.restaurant_voting.model.AbstractBaseEntity;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.repository.MealRepository;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("deprecation")
@Service
public class InitiateUtil implements CommandLineRunner {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MealRepository mealRepository;

    public static final User user1 = new User(null,"Guest","guest@gmail.com",
            "{noop}guest",new Date(2022, Calendar.DECEMBER,10));

    public static final Restaurant rest1 = new Restaurant(null, "Tasty Island");
    public static final Restaurant rest2 = new Restaurant(null, "Burger Master");
    public static final Restaurant rest3 = new Restaurant(null, "Sushi city");

    public static final Meal meal1 =
            new Meal(null, "Soup", 50, new Restaurant(AbstractBaseEntity.START_SEQ, rest1.getName()));
    public static final Meal meal2 =
            new Meal(null, "Green salt", 100, new Restaurant(AbstractBaseEntity.START_SEQ, rest1.getName()));
    public static final Meal meal3 =
            new Meal(null, "Red Fish", 300, new Restaurant(AbstractBaseEntity.START_SEQ, rest1.getName()));
    public static final Meal meal4 =
            new Meal(null, "Hamburger", 100, new Restaurant(AbstractBaseEntity.START_SEQ + 1, rest2.getName()));
    public static final Meal meal5 =
            new Meal(null, "Coca-cola", 150, new Restaurant(AbstractBaseEntity.START_SEQ + 1, rest2.getName()));
    public static final Meal meal6 =
            new Meal(null, "Fry Potato", 120, new Restaurant(AbstractBaseEntity.START_SEQ + 1, rest2.getName()));
    public static final Meal meal7 =
            new Meal(null, "Classic Sushi", 200, new Restaurant(AbstractBaseEntity.START_SEQ + 2, rest3.getName()));
    public static final Meal meal8 =
            new Meal(null, "Wok", 180, new Restaurant(AbstractBaseEntity.START_SEQ + 2, rest3.getName()));

    @Override
    public void run(String... args) {
        restaurantRepository.save(rest1);
        restaurantRepository.save(rest2);
        restaurantRepository.save(rest3);
        mealRepository.save(meal1);
        mealRepository.save(meal2);
        mealRepository.save(meal3);
        mealRepository.save(meal4);
        mealRepository.save(meal5);
        mealRepository.save(meal6);
        mealRepository.save(meal7);
        mealRepository.save(meal8);
    }
}
