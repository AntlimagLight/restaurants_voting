package com.topjava.restaurant_voting.controller.users;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.UserUtils.STARTING_ROLES;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegisterController {

    private final UserService userService;

    @Operation(
            summary = "User registration",
            description = "Registers a user and adds it to the database. " +
                    "The user will automatically be assigned the USER role."
    )
    @PostMapping()
    public ResponseEntity<URI> registration(@Valid @RequestBody User user) {
        log.info("registration {} {}", USER_ENTITY_NAME, user.getEmail());
        user.setRoles(STARTING_ROLES);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/users/{id}")
                .buildAndExpand(userService.create(user).getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(uriOfNewResource);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError badRequestHandle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError alreadyExistHandle(AlreadyExistException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
