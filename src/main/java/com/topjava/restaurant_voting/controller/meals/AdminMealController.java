package com.topjava.restaurant_voting.controller.meals;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/restaurants/{restaurant_id}/menu")
public class AdminMealController {

    private final MealService mealService;

    @PostMapping()
    public ResponseEntity<URI> createMeal(@Valid @RequestBody Meal meal, @PathVariable Integer restaurant_id) {
        log.info("create {} {} in {} {}", MEAL_ENTITY_NAME, meal.getName(), RESTAURANT_ENTITY_NAME, restaurant_id);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/restaurants/" + restaurant_id + "/menu/{id}")
                .buildAndExpand(mealService.create(meal, restaurant_id).getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(uriOfNewResource);
    }

    @PutMapping("/{meal_id}")
    public ResponseEntity<String> updateMeal(@Valid @RequestBody Meal meal, @PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        log.info("update {} {} in {} {}", MEAL_ENTITY_NAME, meal.getName(), RESTAURANT_ENTITY_NAME, restaurant_id);
        mealService.update(meal, meal_id, restaurant_id);
        return ResponseEntity.status(204).body(MEAL_ENTITY_NAME + " updated:\n" + meal.getId());
    }

    @DeleteMapping("/{meal_id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        log.info("delete {} {} in {} {}", MEAL_ENTITY_NAME, meal_id, RESTAURANT_ENTITY_NAME, restaurant_id);
        mealService.delete(meal_id, restaurant_id);
        return ResponseEntity.status(204).body(MEAL_ENTITY_NAME + " deleted:\n" + meal_id);
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError alreadyExistHandle(AlreadyExistException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
