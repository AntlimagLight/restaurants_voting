package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            restaurantService.create(restaurant);
            return ResponseEntity.ok(RESTAURANT_ENTITY_NAME + " saved:\n" + restaurant);
        } catch (AlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable Integer id) {
        try {
            restaurantService.update(id, restaurant);
            return ResponseEntity.ok(RESTAURANT_ENTITY_NAME + " updated:\n" + restaurant);
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getRestaurant(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(restaurantService.getById(id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRestaurant(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(RESTAURANT_ENTITY_NAME + " deleted:\n" + restaurantService.delete(id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping
    public ResponseEntity getAll() {
        try {
            return ResponseEntity.ok(restaurantService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}
