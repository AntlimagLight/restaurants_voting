package com.topjava.restaurant_voting.util;

import com.topjava.restaurant_voting.model.*;
import com.topjava.restaurant_voting.repository.MealRepository;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "OptionalGetWithoutIsPresent"})
@Service
public class InitiateUtil implements CommandLineRunner {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteService voteService;

    public static final LocalTime VOTING_END_TIME = LocalTime.of(23, 0);

    public static final User GUEST = new User(null, "Guest", "guest@gmail.com",
            "{noop}guest", LocalDate.of(2022, Calendar.DECEMBER, 10));
    public static final User USER = new User(null, "User", "user@yandex.ru",
            "{noop}password", LocalDate.of(2022, Calendar.DECEMBER, 15), Role.USER);
    public static final User ADMIN = new User(null, "Admin", "admin@gmail.com",
            "{noop}admin", LocalDate.of(2022, Calendar.DECEMBER, 20), Role.USER, Role.ADMIN);

    public static final Restaurant REST_1 = new Restaurant(null, "Tasty Island");
    public static final Restaurant REST_2 = new Restaurant(null, "Burger Master");
    public static final Restaurant REST_3 = new Restaurant(null, "Sushi city");

    public static final Meal MEAL_1 =
            new Meal(null, "Soup", 50, new Restaurant(AbstractBaseEntity.START_SEQ + 3, REST_1.getName()));
    public static final Meal MEAL_2 =
            new Meal(null, "Green salt", 100, new Restaurant(AbstractBaseEntity.START_SEQ + 3, REST_1.getName()));
    public static final Meal MEAL_3 =
            new Meal(null, "Red Fish", 300, new Restaurant(AbstractBaseEntity.START_SEQ + 3, REST_1.getName()));
    public static final Meal MEAL_4 =
            new Meal(null, "Hamburger", 100, new Restaurant(AbstractBaseEntity.START_SEQ + 4, REST_2.getName()));
    public static final Meal MEAL_5 =
            new Meal(null, "Coca-cola", 150, new Restaurant(AbstractBaseEntity.START_SEQ + 4, REST_2.getName()));
    public static final Meal MEAL_6 =
            new Meal(null, "Fry Potato", 120, new Restaurant(AbstractBaseEntity.START_SEQ + 4, REST_2.getName()));
    public static final Meal MEAL_7 =
            new Meal(null, "Classic Sushi", 200, new Restaurant(AbstractBaseEntity.START_SEQ + 5, REST_3.getName()));
    public static final Meal MEAL_8 =
            new Meal(null, "Wok", 180, new Restaurant(AbstractBaseEntity.START_SEQ + 5, REST_3.getName()));

    @Override
    public void run(String... args) {
        userRepository.save(GUEST);
        userRepository.save(USER);
        userRepository.save(ADMIN);
        restaurantRepository.save(REST_1);
        restaurantRepository.save(REST_2);
        restaurantRepository.save(REST_3);
        mealRepository.save(MEAL_1);
        mealRepository.save(MEAL_2);
        mealRepository.save(MEAL_3);
        mealRepository.save(MEAL_4);
        mealRepository.save(MEAL_5);
        mealRepository.save(MEAL_6);
        mealRepository.save(MEAL_7);
        mealRepository.save(MEAL_8);
        voteService.makeVote(userRepository.findById(100001).get(), 100003);
        voteService.makeVote(userRepository.findById(100002).get(), 100004);
    }
}
