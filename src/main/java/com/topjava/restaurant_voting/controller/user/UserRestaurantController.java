package com.topjava.restaurant_voting.controller.user;

import com.topjava.restaurant_voting.dto.RestaurantDto;
import com.topjava.restaurant_voting.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("get {} {}", RESTAURANT_ENTITY_NAME, restaurant_id);
        return ResponseEntity.ok(restaurantService.getById(restaurant_id));
    }

    @Operation(
            summary = "Get All Restaurants",
            description = "In response to the request, a list of all restaurants from the database was sent."
    )
    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<RestaurantDto>> getAll() {
        log.info("get all {}", RESTAURANT_ENTITY_NAME);
        return ResponseEntity.ok(restaurantService.getAll());
    }

}
