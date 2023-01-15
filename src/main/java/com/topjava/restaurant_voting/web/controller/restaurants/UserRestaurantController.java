package com.topjava.restaurant_voting.web.controller.restaurants;

import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;

@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection", "DuplicatedCode"})
@RestController
@RequestMapping("/user/restaurants")
public class UserRestaurantController {
    private static final Logger log = LoggerFactory.getLogger(UserRestaurantController.class);
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{restaurant_id}")
    public ResponseEntity getRestaurant(@PathVariable Integer restaurant_id) {
        try {
            log.info("get " + RESTAURANT_ENTITY_NAME + " " + restaurant_id);
            return ResponseEntity.ok(restaurantService.getById(restaurant_id));
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
