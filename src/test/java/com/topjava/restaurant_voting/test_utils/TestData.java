package com.topjava.restaurant_voting.test_utils;

import com.topjava.restaurant_voting.dto.VoteDto;
import com.topjava.restaurant_voting.model.*;
import com.topjava.restaurant_voting.util.JsonUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.util.InitiateDataUtil.*;


public class TestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");
    public static final MatcherFactory.Matcher<User> USER_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(User.class, "password", "votes", "registered");
    @SuppressWarnings("rawtypes")
    public static final MatcherFactory.Matcher<HashMap> MAP_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(HashMap.class);
    public static final MatcherFactory.Matcher<VoteDto> VOTE_DTO_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(VoteDto.class, "user");

    public static final User USER_100000 = setIdForTests(USER1, START_SEQ);
    public static final User USER_100001 = setIdForTests(USER2, START_SEQ + 1);
    public static final User USER_100002 = setIdForTests(ADMIN, START_SEQ + 2);
    public static final User DUPLICATE_EMAIL_USER = new User(null, "Doppelganger", USER_100000.getEmail(),
            "pass1", null, true, Role.USER);
    public static final User NEW_USER = new User(null, "NewUser", "newuser@yandex.ru",
            "newpass", null, true, Role.USER);
    public static final User NEW_ADMIN = new User(null, "NewAdmin", "newadmin@yandex.ru",
            "newadminpass", null, true, Role.USER, Role.ADMIN);
    public static final User UPD_USER = new User(null, "Updated", "updmail@gmail.com",
            "updpass12", null, true, Role.USER);
    public static final User UPD_USER_TO_ADMIN = new User(null, "Updated", "updadmin@gmail.com",
            "updpass12", null, true, Role.USER, Role.ADMIN);
    public static final List<User> ALL_USERS = List.of(USER_100000, USER_100001, USER_100002);

    public static final Restaurant REST_100003 = setIdForTests(REST_1, START_SEQ + 3);
    public static final Restaurant REST_100004 = setIdForTests(REST_2, START_SEQ + 4);
    public static final Restaurant REST_100005 = setIdForTests(REST_3, START_SEQ + 5);
    public static final Restaurant NEW_RESTAURANT = new Restaurant(null, "New Restaurant");
    public static final Restaurant UPDATED_RESTAURANT = new Restaurant(null, "Upd Restaurant");
    public static final List<Restaurant> ALL_RESTAURANTS = List.of(REST_100003, REST_100004, REST_100005);

    public static final Meal MEAL_100006 = setIdForTests(MEAL_1, START_SEQ + 6);
    public static final Meal MEAL_100007 = setIdForTests(MEAL_2, START_SEQ + 7);
    public static final Meal MEAL_100008 = setIdForTests(MEAL_3, START_SEQ + 8);
    public static final Meal MEAL_100009 = setIdForTests(MEAL_4, START_SEQ + 9);
    public static final Meal MEAL_100010 = setIdForTests(MEAL_5, START_SEQ + 10);
    public static final Meal MEAL_100011 = setIdForTests(MEAL_6, START_SEQ + 11);
    public static final Meal MEAL_100012 = setIdForTests(MEAL_7, START_SEQ + 12);
    public static final Meal MEAL_100013 = setIdForTests(MEAL_8, START_SEQ + 13);
    public static final Meal NEW_MEAL = new Meal(null, "New Meal", 111, null);
    public static final Meal UPDATED_MEAL = new Meal(null, "Upd Meal", 99, null);
    public static final List<Meal> ALL_MEALS_FROM_REST_100004 = List.of(MEAL_100009, MEAL_100010, MEAL_100011);

    public static final VoteDto VOTE_DTO_1 = new VoteDto(null, USER_100000,
            REST_100004, LocalDate.of(2021, 12, 1));
    public static final VoteDto VOTE_DTO_2 = new VoteDto(null, USER_100000,
            REST_100005, LocalDate.of(2022, 6, 10));

    public final static Map<String, Integer> STATISTIC = new HashMap<>();

    static {
        STATISTIC.put(REST_100005.getName(), 1);
        STATISTIC.put(REST_100003.getName(), 1);
    }

    public static <T extends AbstractBaseEntity> T setIdForTests(T entity, Integer Id) {
        entity.setId(Id);
        return entity;
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}
