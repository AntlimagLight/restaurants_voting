package com.topjava.restaurant_voting.controller.restaurants;

import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "Get One Restaurant",
            description = "In response to the request, a restaurant with {restaurant_id} will be sent."
    )
    @GetMapping("/{restaurant_id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("get {} {}", RESTAURANT_ENTITY_NAME, restaurant_id);
        return ResponseEntity.ok(restaurantService.getById(restaurant_id));
    }

    @Operation(
            summary = "Get All Restaurants",
            description = "In response to the request, a list of all restaurants from the database was sent."
    )
    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<Restaurant>> getAll() {
        log.info("get all {}", RESTAURANT_ENTITY_NAME);
        return ResponseEntity.ok(restaurantService.getAll());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError badRequestHandle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError notExistHandle(NotExistException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
