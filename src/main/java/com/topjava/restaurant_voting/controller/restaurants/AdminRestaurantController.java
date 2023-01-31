package com.topjava.restaurant_voting.controller.restaurants;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;

@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection", "DuplicatedCode"})
@RestController
@RequestMapping("/admin/restaurants")
@Slf4j
public class AdminRestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            log.info("create {} {}", RESTAURANT_ENTITY_NAME, restaurant.getName());
            restaurantService.create(restaurant);
            return ResponseEntity.ok(RESTAURANT_ENTITY_NAME + " saved:\n" + restaurant);
        } catch (AlreadyExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("/{restaurant_id}")
    public ResponseEntity updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable Integer restaurant_id) {
        try {
            log.info("update {} {}", RESTAURANT_ENTITY_NAME, restaurant.getName());
            restaurantService.update(restaurant_id, restaurant);
            return ResponseEntity.ok(RESTAURANT_ENTITY_NAME + " updated:\n" + restaurant);
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @DeleteMapping("/{restaurant_id}")
    public ResponseEntity deleteRestaurant(@PathVariable Integer restaurant_id) {
        try {
            log.info("delete {} {}", RESTAURANT_ENTITY_NAME, restaurant_id);
            return ResponseEntity.ok(RESTAURANT_ENTITY_NAME + " deleted:\n" + restaurantService.delete(restaurant_id));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}