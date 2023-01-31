package com.topjava.restaurant_voting.controller.meals;

import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.service.MealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.MealService.MEAL_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;


@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection", "DuplicatedCode"})
@RestController
@Slf4j
@RequestMapping("/user/restaurants/{restaurant_id}/menu")
public class UserMealController {
    @Autowired
    private MealService mealService;


    @GetMapping("/{meal_id}")
    public ResponseEntity getMeal(@PathVariable Integer restaurant_id, @PathVariable Integer meal_id) {
        try {
            log.info("get {} {} from {} {}", MEAL_ENTITY_NAME, meal_id, RESTAURANT_ENTITY_NAME, restaurant_id);
            return ResponseEntity.ok(mealService.getById(meal_id, restaurant_id));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("")
    public ResponseEntity getMenu(@PathVariable Integer restaurant_id) {
        try {
            log.info("get all {} in {} {}", MEAL_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
            return ResponseEntity.ok(mealService.getRestaurantMenu(restaurant_id));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}
