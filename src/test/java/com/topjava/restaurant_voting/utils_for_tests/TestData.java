package com.topjava.restaurant_voting.utils_for_tests;

import com.topjava.restaurant_voting.dto.*;
import com.topjava.restaurant_voting.mapper.MealMapper;
import com.topjava.restaurant_voting.mapper.RestaurantMapper;
import com.topjava.restaurant_voting.mapper.UserMapper;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.Role;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.util.JsonUtil;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.util.InitiateDataUtil.*;


public class TestData {
    public static final RestaurantMapper restaurantMapper = Mappers.getMapper(RestaurantMapper.class);
    public static final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    public static final MealMapper mealMapper = Mappers.getMapper(MealMapper.class);
    public static final MatcherFactory.Matcher<RestaurantDto> RESTAURANT_DTO_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator();
    public static final MatcherFactory.Matcher<MealDto> MEAL_DTO_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator("restaurant", "date");
    public static final MatcherFactory.Matcher<UserDto> USER_DTO_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator("password", "registered");

    public static final UserDto USER_100000 = setIdForTests(userMapper.toDTO(USER1), START_SEQ);
    public static final UserDto USER_100001 = setIdForTests(userMapper.toDTO(USER2), START_SEQ + 1);
    public static final UserDto USER_100002 = setIdForTests(userMapper.toDTO(ADMIN), START_SEQ + 2);
    public static final User DUPLICATE_EMAIL_USER = new User(null, "Doppelganger", USER_100000.getEmail(),
            "pass1", null, true, Role.USER);
    public static final UserDto DUPLICATE_EMAIL_USER_DTO = userMapper.toDTO(DUPLICATE_EMAIL_USER);
    public static final User NEW_USER = new User(null, "NewUser", "newuser@yandex.ru",
            "newpass", null, true, Role.USER);
    public static final UserDto NEW_USER_DTO = userMapper.toDTO(NEW_USER);
    public static final User NEW_ADMIN = new User(null, "NewAdmin", "newadmin@yandex.ru",
            "newadminpass", null, true, Role.USER, Role.ADMIN);
    public static final UserDto NEW_ADMIN_DTO = sortRoles(userMapper.toDTO(NEW_ADMIN));
    public static final User UPD_USER = new User(null, "Updated", "updmail@gmail.com",
            "updpass12", null, true, Role.USER);
    public static final UserDto UPD_USER_DTO = userMapper.toDTO(UPD_USER);
    public static final User UPD_USER_TO_ADMIN = new User(null, "Updated", "updadmin@gmail.com",
            "updpass12", null, true, Role.USER, Role.ADMIN);
    public static final UserDto UPD_USER_TO_ADMIN_DTO = sortRoles(userMapper.toDTO(UPD_USER_TO_ADMIN));
    public static final List<UserDto> ALL_USERS = List.of(USER_100000, USER_100001, USER_100002);

    public static final RestaurantDto REST_100003 = setIdForTests(restaurantMapper.toDTO(REST_1), START_SEQ + 3);
    public static final RestaurantDto REST_100004 = setIdForTests(restaurantMapper.toDTO(REST_2), START_SEQ + 4);
    public static final RestaurantDto REST_100005 = setIdForTests(restaurantMapper.toDTO(REST_3), START_SEQ + 5);
    public static final Restaurant NEW_RESTAURANT = new Restaurant(null, "New Restaurant");
    public static final RestaurantDto NEW_RESTAURANT_DTO = restaurantMapper.toDTO(NEW_RESTAURANT);
    public static final Restaurant UPDATED_RESTAURANT = new Restaurant(null, "Upd Restaurant");
    public static final RestaurantDto UPDATED_RESTAURANT_DTO = restaurantMapper.toDTO(UPDATED_RESTAURANT);
    public static final List<RestaurantDto> ALL_RESTAURANTS = List.of(REST_100003, REST_100004, REST_100005);

    public static final MealDto MEAL_100006 = setIdForTests(mealMapper.toDTO(MEAL_1), START_SEQ + 6);
    public static final MealDto MEAL_100007 = setIdForTests(mealMapper.toDTO(MEAL_2), START_SEQ + 7);
    public static final MealDto MEAL_100008 = setIdForTests(mealMapper.toDTO(MEAL_3), START_SEQ + 8);
    public static final MealDto MEAL_100009 = setIdForTests(mealMapper.toDTO(MEAL_4), START_SEQ + 9);
    public static final MealDto MEAL_100010 = setIdForTests(mealMapper.toDTO(MEAL_5), START_SEQ + 10);
    public static final MealDto MEAL_100011 = setIdForTests(mealMapper.toDTO(MEAL_6), START_SEQ + 11);
    public static final MealDto MEAL_100012 = setIdForTests(mealMapper.toDTO(MEAL_7), START_SEQ + 12);
    public static final MealDto MEAL_100013 = setIdForTests(mealMapper.toDTO(MEAL_8), START_SEQ + 13);
    public static final Meal NEW_MEAL = new Meal(null, "New Meal", 111, null, null);
    public static final MealDto NEW_MEAL_DTO = mealMapper.toDTO(NEW_MEAL);
    public static final Meal NEW_MEAL_ALREADY_EXIST =
            new Meal(null, "Hamburger", 100, MEAL_4.getRestaurant(), null);
    public static final MealDto NEW_MEAL_ALREADY_EXIST_DTO = mealMapper.toDTO(NEW_MEAL_ALREADY_EXIST);
    public static final Meal UPDATED_MEAL = new Meal(null, "Upd Meal", 99, null, null);
    public static final MealDto UPDATED_MEAL_DTO = mealMapper.toDTO(UPDATED_MEAL);
    public static final List<MealDto> ALL_MEALS_FROM_REST_100004 = List.of(MEAL_100009, MEAL_100010, MEAL_100011);
    public final static List<RestaurantWithMenuDto> ALL_REST_MENU = new ArrayList<>();

    static {
        List<MealDto> REST_100003_TODAY_MENU = new ArrayList<>();
        REST_100003_TODAY_MENU.add(MEAL_100006);
        REST_100003_TODAY_MENU.add(MEAL_100008);
        ALL_REST_MENU.add(new RestaurantWithMenuDto(REST_100003.getId(), REST_100003.getName(), REST_100003_TODAY_MENU));
        List<MealDto> REST_100004_TODAY_MENU = new ArrayList<>();
        REST_100004_TODAY_MENU.add(MEAL_100009);
        REST_100004_TODAY_MENU.add(MEAL_100010);
        REST_100004_TODAY_MENU.add(MEAL_100011);
        ALL_REST_MENU.add(new RestaurantWithMenuDto(REST_100004.getId(), REST_100004.getName(), REST_100004_TODAY_MENU));
        List<MealDto> REST_100005_TODAY_MENU = new ArrayList<>();
        REST_100005_TODAY_MENU.add(MEAL_100012);
        ALL_REST_MENU.add(new RestaurantWithMenuDto(REST_100005.getId(), REST_100005.getName(), REST_100005_TODAY_MENU));
    }

    public static final VoteDto VOTE_DTO_1 = new VoteDto(START_SEQ + 14, USER_100000.getId(),
            REST_100004.getId(), LocalDate.of(2021, 12, 1));
    public static final VoteDto VOTE_DTO_2 = new VoteDto(START_SEQ + 15, USER_100000.getId(),
            REST_100005.getId(), LocalDate.of(2022, 6, 10));
    public final static List<VoteDto> VOTES_BY_USER100000 = new ArrayList<>();
    public final static List<VoteCountDtoImpl> STATISTIC = new ArrayList<>();

    static {
        VOTES_BY_USER100000.add(VOTE_DTO_1);
        VOTES_BY_USER100000.add(VOTE_DTO_2);
        STATISTIC.add(new VoteCountDtoImpl(REST_100003.getId(), REST_100003.getName(), 2));
        STATISTIC.add(new VoteCountDtoImpl(REST_100005.getId(), REST_100005.getName(), 1));
    }

    public static <T extends AbstractNamedDto> T setIdForTests(T dto, Long id) {
        dto.setId(id);
        return dto;
    }

    public static UserDto sortRoles(UserDto userDto) {
        userDto.setRoles(userDto.getRoles().stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return userDto;
    }

    public static String jsonWithPassword(UserDto user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}
