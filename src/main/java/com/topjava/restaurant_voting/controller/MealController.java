package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;


@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping("/admin/restaurant")
public class MealController {

    @Autowired
    private MealService mealService;

    @PostMapping("/{restaurant_id}")
    public ResponseEntity createMeal(@RequestBody Meal meal, @PathVariable Integer restaurant_id) {
        try {
            mealService.create(meal, restaurant_id);
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " saved:\n" + meal);
        } catch (AlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("/{restaurant_id}/menu/{meal_id}")
    public ResponseEntity updateMeal(@RequestBody Meal meal, @PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        try {
            mealService.update(meal, meal_id, restaurant_id);
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " updated:\n" + meal);
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/{restaurant_id}/menu/{meal_id}")
    public ResponseEntity getMeal(@PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        try {
            return ResponseEntity.ok(mealService.getById(meal_id, restaurant_id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @DeleteMapping("/{restaurant_id}/menu/{meal_id}")
    public ResponseEntity deleteRestaurant(@PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        try {
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " deleted:\n" + mealService.delete(meal_id, restaurant_id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/{restaurant_id}/menu")
    public ResponseEntity getAll(@PathVariable Integer restaurant_id) {
        try {
            return ResponseEntity.ok(mealService.getRestaurantMenu(restaurant_id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}
