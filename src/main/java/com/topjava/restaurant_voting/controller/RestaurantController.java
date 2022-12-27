package com.topjava.restaurant_voting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @GetMapping
    public ResponseEntity getRestaurant() {
        try {
            return ResponseEntity.ok("Все работает");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");

        }
    }
}
