package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;

@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection", "DuplicatedCode"})
@RestController
@RequestMapping("/admin/restaurant")
public class RestaurantController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            log.info("create " + RESTAURANT_ENTITY_NAME + " " + restaurant.getName());
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

    @PutMapping("/{id}")
    public ResponseEntity updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable Integer id) {
        try {
            log.info("update " + RESTAURANT_ENTITY_NAME + " " + restaurant.getName());
            restaurantService.update(id, restaurant);
            return ResponseEntity.ok(RESTAURANT_ENTITY_NAME + " updated:\n" + restaurant);
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getRestaurant(@PathVariable Integer id) {
        try {
            log.info("get " + RESTAURANT_ENTITY_NAME + " " + id);
            return ResponseEntity.ok(restaurantService.getById(id));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRestaurant(@PathVariable Integer id) {
        try {
            log.info("delete " + RESTAURANT_ENTITY_NAME + " " + id);
            return ResponseEntity.ok(RESTAURANT_ENTITY_NAME + " deleted:\n" + restaurantService.delete(id));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping
    public ResponseEntity getAll() {
        try {
            log.info("get all " + RESTAURANT_ENTITY_NAME);
            return ResponseEntity.ok(restaurantService.getAll());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}
