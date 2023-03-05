package com.topjava.restaurant_voting.controller.admin;

import com.topjava.restaurant_voting.dto.RestaurantDto;
import com.topjava.restaurant_voting.mapper.RestaurantMapper;
import com.topjava.restaurant_voting.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
    private final RestaurantMapper restaurantMapper;

    @Operation(
            summary = "Create Restaurant",
            description = "Registers a new restaurant in the database."
    )
    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<RestaurantDto> createRestaurant(@Valid @RequestBody RestaurantDto restaurant) {
        log.info("create {} {}", RESTAURANT_ENTITY_NAME, restaurant.getName());
        val created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(restaurantMapper.toDTO(created));
    }

    @Operation(
            summary = "Update Restaurant",
            description = "Updates the restaurant with {restaurant_id} in the database"
    )
    @PutMapping("/{restaurant_id}")
    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRestaurant(@Valid @RequestBody RestaurantDto restaurant,
                                 @PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("update {} {}", RESTAURANT_ENTITY_NAME, restaurant.getName());
        restaurantService.update(restaurant_id, restaurant);
    }

    @Operation(
            summary = "Delete Restaurant",
            description = "Removes the restaurant with {restaurant_id} from the database."
    )
    @DeleteMapping("/{restaurant_id}")
    @SecurityRequirement(name = "basicAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable @Parameter(example = "100004") Long restaurant_id) {
        log.info("delete {} {}", RESTAURANT_ENTITY_NAME, restaurant_id);
        restaurantService.delete(restaurant_id);
    }

}
