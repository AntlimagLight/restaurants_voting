package com.topjava.restaurant_voting.controller.meals;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.service.MealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;


@SuppressWarnings({"rawtypes"})
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/restaurants/{restaurant_id}/menu")
public class AdminMealController {

    private final MealService mealService;

    @PostMapping()
    public ResponseEntity createMeal(@RequestBody Meal meal, @PathVariable Integer restaurant_id) {
        try {
            log.info("create {} {} in {} {}", MEAL_ENTITY_NAME, meal.getName(), RESTAURANT_ENTITY_NAME, restaurant_id);
            mealService.create(meal, restaurant_id);
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " saved:\n" + meal);
        } catch (AlreadyExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("/{meal_id}")
    public ResponseEntity updateMeal(@RequestBody Meal meal, @PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        try {
            log.info("update {} {} in {} {}", MEAL_ENTITY_NAME, meal.getName(), RESTAURANT_ENTITY_NAME, restaurant_id);
            mealService.update(meal, meal_id, restaurant_id);
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " updated:\n" + meal);
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @DeleteMapping("/{meal_id}")
    public ResponseEntity deleteRestaurant(@PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        try {
            log.info("delete {} {} in {} {}", MEAL_ENTITY_NAME, meal_id, RESTAURANT_ENTITY_NAME, restaurant_id);
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " deleted:\n" + mealService.delete(meal_id, restaurant_id));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

}
