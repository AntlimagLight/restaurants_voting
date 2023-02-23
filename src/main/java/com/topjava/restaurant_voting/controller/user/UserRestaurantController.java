package com.topjava.restaurant_voting.controller.user;

import com.topjava.restaurant_voting.dto.RestaurantDto;
import com.topjava.restaurant_voting.dto.RestaurantWithMenuDto;
import com.topjava.restaurant_voting.service.MealService;
import com.topjava.restaurant_voting.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;


@RestController
@Slf4j
@RequestMapping("/user/restaurants")
@RequiredArgsConstructor
public class UserRestaurantController {

    private final RestaurantService restaurantService;
    private final MealService mealService;

    @Operation(
            summary = "Get One Restaurant",
            description = "In response to the request, a restaurant with {restaurant_id} will be sent."
    )
    @GetMapping("/{restaurant_id}")
    @SecurityRequirement(name = "basicAuth")
    public RestaurantDto getRestaurant(@PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("get {} {}", RESTAURANT_ENTITY_NAME, restaurant_id);
        return restaurantService.getById(restaurant_id);
    }

    @Operation(
            summary = "Get All Restaurants",
            description = "In response to the request, a list of all restaurants from the database was sent."
    )
    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    public List<RestaurantDto> getAll() {
        log.info("get all {}", RESTAURANT_ENTITY_NAME);
        return restaurantService.getAll();
    }

    @Operation(
            summary = "Get Today Menu in All Restaurants",
            description = "In response to the request will receive a Map with today's menu of each restaurant."
    )
    @GetMapping("/today_menu")
    @SecurityRequirement(name = "basicAuth")
    public List<RestaurantWithMenuDto> getTodayMenu() {
        log.info("get all {} today", MEAL_ENTITY_NAME);
        return mealService.getAllByDateWithRestaurants(LocalDate.now());
    }

}
