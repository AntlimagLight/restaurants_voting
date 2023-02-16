package com.topjava.restaurant_voting.controller.meals;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "Create Meal",
            description = "Registers a new meal owned by the restaurant {restaurant_id} in the database."
    )
    @PostMapping()
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<URI> createMeal(@Valid @RequestBody Meal meal,
                                          @PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("create {} {} in {} {}", MEAL_ENTITY_NAME, meal.getName(), RESTAURANT_ENTITY_NAME, restaurant_id);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/restaurants/" + restaurant_id + "/menu/{id}")
                .buildAndExpand(mealService.create(meal, restaurant_id).getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(uriOfNewResource);
    }

    @Operation(
            summary = "Update Meal",
            description = "Updates the meal owned by the restaurant ID in the database."
    )
    @PutMapping("/{meal_id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> updateMeal(@Valid @RequestBody Meal meal,
                                             @PathVariable @Parameter(example = "100004") Long restaurant_id,
                                             @PathVariable @Parameter(example = "100009") Long meal_id) {
        log.info("update {} {} in {} {}", MEAL_ENTITY_NAME, meal.getName(), RESTAURANT_ENTITY_NAME, restaurant_id);
        mealService.update(meal, meal_id, restaurant_id);
        return ResponseEntity.status(204).body(null);
    }

    @Operation(
            summary = "Delete Meal",
            description = "Removes the meal owned by the restaurant ID in the database."
    )
    @DeleteMapping("/{meal_id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> deleteRestaurant(@PathVariable @Parameter(example = "100004") Long restaurant_id,
                                                   @PathVariable @Parameter(example = "100009") Long meal_id) {
        log.info("delete {} {} in {} {}", MEAL_ENTITY_NAME, meal_id, RESTAURANT_ENTITY_NAME, restaurant_id);
        mealService.delete(meal_id, restaurant_id);
        return ResponseEntity.status(204).body(null);
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
