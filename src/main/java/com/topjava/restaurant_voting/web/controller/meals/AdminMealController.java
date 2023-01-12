package com.topjava.restaurant_voting.web.controller.meals;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Meal;
import com.topjava.restaurant_voting.service.MealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;


@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping("/admin/restaurant/{restaurant_id}/menu")
public class AdminMealController {
    private static final Logger log = LoggerFactory.getLogger(AdminMealController.class);
    @Autowired
    private MealService mealService;

    @PostMapping()
    public ResponseEntity createMeal(@RequestBody Meal meal, @PathVariable Integer restaurant_id) {
        try {
            log.info("create " + MEAL_ENTITY_NAME + " " + meal.getName() +
                    " in " + RESTAURANT_ENTITY_NAME + " " + restaurant_id);
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
            log.info("update " + MEAL_ENTITY_NAME + " " + meal.getName() +
                    " in " + RESTAURANT_ENTITY_NAME + " " + restaurant_id);
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
            log.info("delete " + MEAL_ENTITY_NAME + " " + meal_id +
                    " in " + RESTAURANT_ENTITY_NAME + " " + restaurant_id);
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
