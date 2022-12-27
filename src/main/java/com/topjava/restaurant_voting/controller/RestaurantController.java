package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            restaurantService.createRestaurant(restaurant);
            return ResponseEntity.ok("Сохранено: " + restaurant);
        } catch (AlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Некорректный запрос");
        }
    }

    @GetMapping
    public ResponseEntity getRestaurant() {
        try {
            return ResponseEntity.ok("Все работает");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Некорректный запрос");
        }
    }
}
