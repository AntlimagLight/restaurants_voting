package com.topjava.restaurant_voting.controller.meals;

import com.topjava.restaurant_voting.dto.RestaurantWithMenuDto;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/restaurants/")
public class UserMealController {

    private final MealService mealService;

    @Operation(
            summary = "Get One Meal",
            description = "In response to the request, a meal with {meal_id} belonging " +
                    "to the restaurant with {restaurant_id} will be sent."
    )
    @GetMapping("{restaurant_id}/menu/{meal_id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<Meal> getMeal(@PathVariable @Parameter(example = "100004") Long restaurant_id,
                                        @PathVariable @Parameter(example = "100009") Long meal_id) {
        log.info("get {} {} from {} {}", MEAL_ENTITY_NAME, meal_id, RESTAURANT_ENTITY_NAME, restaurant_id);
        return ResponseEntity.ok(mealService.getById(meal_id, restaurant_id));
    }

    @Operation(
            summary = "Get Restaurant Menu",
            description = "In response to the request, you will receive a complete list of food" +
                    " belonging to the restaurant with {restaurant_id}."
    )
    @GetMapping("{restaurant_id}/menu")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<Meal>> getMenu(@PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("get all {} in {} {}", MEAL_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
        return ResponseEntity.ok(mealService.getRestaurantMenu(restaurant_id));
    }

    @Operation(
            summary = "Get Today Menu in All Restaurants",
            description = "In response to the request will receive a Map with today's menu of each restaurant."
    )
    @GetMapping("/today_menu")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<RestaurantWithMenuDto>> getTodayMenu() {
        log.info("get all {} today", MEAL_ENTITY_NAME);
        return ResponseEntity.ok(mealService.getAllByDateWithRestaurants(LocalDate.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError badRequestHandle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError notExistHandle(NotExistException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
