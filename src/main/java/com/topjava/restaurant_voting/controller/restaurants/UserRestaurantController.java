package com.topjava.restaurant_voting.controller.restaurants;

import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;


@RestController
@Slf4j
@RequestMapping("/user/restaurants")
@RequiredArgsConstructor
public class UserRestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Integer restaurant_id) {
        log.info("get {} {}", RESTAURANT_ENTITY_NAME, restaurant_id);
        return ResponseEntity.ok(restaurantService.getById(restaurant_id));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll() {
        log.info("get all {}", RESTAURANT_ENTITY_NAME);
        return ResponseEntity.ok(restaurantService.getAll());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
