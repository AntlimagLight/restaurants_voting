package com.topjava.restaurant_voting.util;

import com.topjava.restaurant_voting.model.*;
import com.topjava.restaurant_voting.repository.MealRepository;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@SuppressWarnings({"OptionalGetWithoutIsPresent"})
@Service
@RequiredArgsConstructor
public class InitiateDataUtil implements CommandLineRunner {
    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public static final LocalDate START_DATE = LocalDateTime.now().toLocalDate();

    public static final User USER1 = new User(null, "User1", "user_zero@yandex.ru",
            "pass12", LocalDate.of(2022, Calendar.DECEMBER, 10), true, Role.USER);
    public static final User USER2 = new User(null, "User2", "user@yandex.ru",
            "password", LocalDate.of(2022, Calendar.DECEMBER, 15), true, Role.USER);
    public static final User ADMIN = new User(null, "Admin", "admin@gmail.com",
            "admin", LocalDate.of(2022, Calendar.DECEMBER, 20), true, Role.USER, Role.ADMIN);

    public static final Restaurant REST_1 = new Restaurant(null, "Tasty Island");
    public static final Restaurant REST_2 = new Restaurant(null, "Burger Master");
    public static final Restaurant REST_3 = new Restaurant(null, "Sushi city");

    public static final Meal MEAL_1 =
            new Meal(null, "Soup", 50, new Restaurant(AbstractBaseEntity.START_SEQ + 3, REST_1.getName()),
                    START_DATE);
    public static final Meal MEAL_2 =
            new Meal(null, "Green salt", 100, new Restaurant(AbstractBaseEntity.START_SEQ + 3, REST_1.getName()),
                    LocalDate.of(2022, 6, 10));
    public static final Meal MEAL_3 =
            new Meal(null, "Red Fish", 300, new Restaurant(AbstractBaseEntity.START_SEQ + 3, REST_1.getName()),
                    START_DATE);
    public static final Meal MEAL_4 =
            new Meal(null, "Hamburger", 100, new Restaurant(AbstractBaseEntity.START_SEQ + 4, REST_2.getName()),
                    START_DATE);
    public static final Meal MEAL_5 =
            new Meal(null, "Coca-cola", 150, new Restaurant(AbstractBaseEntity.START_SEQ + 4, REST_2.getName()),
                    START_DATE);
    public static final Meal MEAL_6 =
            new Meal(null, "Fry Potato", 120, new Restaurant(AbstractBaseEntity.START_SEQ + 4, REST_2.getName()),
                    START_DATE);
    public static final Meal MEAL_7 =
            new Meal(null, "Classic Sushi", 200, new Restaurant(AbstractBaseEntity.START_SEQ + 5, REST_3.getName()),
                    START_DATE);
    public static final Meal MEAL_8 =
            new Meal(null, "Wok", 180, new Restaurant(AbstractBaseEntity.START_SEQ + 5, REST_3.getName()),
                    LocalDate.of(2022, 6, 10));

    public void saveInitData() {
        userRepository.save(UserUtils.prepareToSave(USER1));
        userRepository.save(UserUtils.prepareToSave(USER2));
        userRepository.save(UserUtils.prepareToSave(ADMIN));
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

        Vote oldVote1 = new Vote(null, userRepository.findById(100000).get(),
                restaurantRepository.findById(100004).get(), LocalDate.of(2021, 12, 1));
        Vote oldVote2 = new Vote(null, userRepository.findById(100000).get(),
                restaurantRepository.findById(100005).get(), LocalDate.of(2022, 6, 10));
        Vote oldVote3 = new Vote(null, userRepository.findById(100001).get(),
                restaurantRepository.findById(100003).get(), LocalDate.of(2022, 6, 10));
        Vote newVote1 = new Vote(null, userRepository.findById(100001).get(),
                restaurantRepository.findById(100004).get(), START_DATE);
        Vote newVote2 = new Vote(null, userRepository.findById(100002).get(),
                restaurantRepository.findById(100003).get(), START_DATE);

        voteRepository.save(oldVote1);
        voteRepository.save(oldVote2);
        voteRepository.save(oldVote3);
        voteRepository.save(newVote1);
        voteRepository.save(newVote2);
    }

    @Override
    public void run(String... args) {
        saveInitData();
    }
}
