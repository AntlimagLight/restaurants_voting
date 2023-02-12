package com.topjava.restaurant_voting.controller.meals;

import com.topjava.restaurant_voting.dto.RestaurantWithMenuDto;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.service.MealService;
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

    @GetMapping("{restaurant_id}/menu/{meal_id}")
    public ResponseEntity<Meal> getMeal(@PathVariable Long restaurant_id, @PathVariable Long meal_id) {
        log.info("get {} {} from {} {}", MEAL_ENTITY_NAME, meal_id, RESTAURANT_ENTITY_NAME, restaurant_id);
        return ResponseEntity.ok(mealService.getById(meal_id, restaurant_id));
    }

    @GetMapping("{restaurant_id}/menu")
    public ResponseEntity<List<Meal>> getMenu(@PathVariable Long restaurant_id) {
        log.info("get all {} in {} {}", MEAL_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
        return ResponseEntity.ok(mealService.getRestaurantMenu(restaurant_id));
    }

    @GetMapping("/today_menu")
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
