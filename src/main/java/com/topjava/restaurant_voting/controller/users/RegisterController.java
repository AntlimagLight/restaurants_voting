package com.topjava.restaurant_voting.controller.users;

import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.UserUtils.STARTING_ROLES;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegisterController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<String> registration(@RequestBody User user) {
        log.info("registration {} {}", USER_ENTITY_NAME, user.getEmail());
        user.setRoles(STARTING_ROLES);
        userService.create(user);
        return ResponseEntity.ok("registration success:\n" + user.getEmail());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
