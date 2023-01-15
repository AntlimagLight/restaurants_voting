package com.topjava.restaurant_voting.web.controller.users;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.UserUtils.STARTING_ROLES;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "rawtypes"})
@RestController
@RequestMapping("/registration")
public class RegisterController {
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity registration(@RequestBody User user) {
        try {
            log.info("registration " + USER_ENTITY_NAME + " " + user.getEmail());
            user.setRoles(STARTING_ROLES);
            userService.create(user);
            return ResponseEntity.ok("registration success:\n" + user.getEmail());
        } catch (AlreadyExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}
