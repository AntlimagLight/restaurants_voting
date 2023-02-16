package com.topjava.restaurant_voting.controller.restaurants;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;

@RestController
@RequestMapping("/admin/restaurants")
@RequiredArgsConstructor
@Slf4j
public class AdminRestaurantController {
    private final RestaurantService restaurantService;

    @Operation(
            summary = "Create Restaurant",
            description = "Registers a new restaurant in the database."
    )
    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<URI> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {} {}", RESTAURANT_ENTITY_NAME, restaurant.getName());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/restaurants/{id}")
                .buildAndExpand(restaurantService.create(restaurant).getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(uriOfNewResource);
    }

    @Operation(
            summary = "Update Restaurant",
            description = "Updates the restaurant with {restaurant_id} in the database"
    )
    @PutMapping("/{restaurant_id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> updateRestaurant(@Valid @RequestBody Restaurant restaurant,
                                                   @PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("update {} {}", RESTAURANT_ENTITY_NAME, restaurant.getName());
        restaurantService.update(restaurant_id, restaurant);
        return ResponseEntity.status(204).body(null);
    }

    @Operation(
            summary = "Delete Restaurant",
            description = "Removes the restaurant with {restaurant_id} from the database."
    )
    @DeleteMapping("/{restaurant_id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> deleteRestaurant(@PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("delete {} {}", RESTAURANT_ENTITY_NAME, restaurant_id);
        restaurantService.delete(restaurant_id);
        return ResponseEntity.status(204).body(null);
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError alreadyExistHandle(AlreadyExistException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
