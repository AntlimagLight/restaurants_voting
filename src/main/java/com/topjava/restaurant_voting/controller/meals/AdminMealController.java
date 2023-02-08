package com.topjava.restaurant_voting.controller.meals;

import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.service.MealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/restaurants/{restaurant_id}/menu")
public class AdminMealController {

    private final MealService mealService;

    @PostMapping()
    public ResponseEntity<String> createMeal(@RequestBody Meal meal, @PathVariable Integer restaurant_id) {
        log.info("create {} {} in {} {}", MEAL_ENTITY_NAME, meal.getName(), RESTAURANT_ENTITY_NAME, restaurant_id);
        mealService.create(meal, restaurant_id);
        return ResponseEntity.ok(MEAL_ENTITY_NAME + " saved:\n" + meal.getName() + " in " + restaurant_id);
    }

    @PutMapping("/{meal_id}")
    public ResponseEntity<String> updateMeal(@RequestBody Meal meal, @PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        log.info("update {} {} in {} {}", MEAL_ENTITY_NAME, meal.getName(), RESTAURANT_ENTITY_NAME, restaurant_id);
        mealService.update(meal, meal_id, restaurant_id);
        return ResponseEntity.ok(MEAL_ENTITY_NAME + " updated:\n" + meal.getId());
    }

    @DeleteMapping("/{meal_id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        log.info("delete {} {} in {} {}", MEAL_ENTITY_NAME, meal_id, RESTAURANT_ENTITY_NAME, restaurant_id);
        mealService.delete(meal_id, restaurant_id);
        return ResponseEntity.ok(MEAL_ENTITY_NAME + " deleted:\n" + meal_id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
