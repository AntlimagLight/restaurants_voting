package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.service.MealService;
import com.topjava.restaurant_voting.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;


@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/restaurant")
public class MealController {

    @Autowired
    private MealService mealService;

    @PostMapping("/{id}")
    public ResponseEntity createMeal(@RequestBody Meal meal, @PathVariable Integer id) {
        try {
            mealService.create(meal, id);
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " saved:\n" + meal);
        } catch (AlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("/{id}/menu")
    public ResponseEntity updateMeal(@RequestBody Meal meal, @PathVariable Integer id, @RequestParam Integer meal_id) {
        try {
            mealService.update(meal, meal_id, id);
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " updated:\n" + meal);
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/{id}/menu/{meal_id}")
    public ResponseEntity getMeal(@PathVariable Integer id, @PathVariable Integer meal_id) {
        try {
            return ResponseEntity.ok(mealService.getById(meal_id, id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @DeleteMapping("/{id}/menu")
    public ResponseEntity deleteRestaurant(@PathVariable Integer id, @RequestParam Integer meal_id) {
        try {
            return ResponseEntity.ok(MEAL_ENTITY_NAME + " deleted:\n" + mealService.delete(meal_id, id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity getAll(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(mealService.getRestaurantMenu(id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}
