package com.topjava.restaurant_voting.controller.users;

import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.security.AuthUser;
import com.topjava.restaurant_voting.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.UserUtils.assureIdConsistent;
import static com.topjava.restaurant_voting.util.UserUtils.convertToRoles;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/user/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get from profile {} {}", USER_ENTITY_NAME, authUser.getEmail());
        return ResponseEntity.ok(userService.getById(authUser.getId()));
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete from profile {} {}", USER_ENTITY_NAME, authUser.getEmail());
        userService.delete(authUser.getId());
        return ResponseEntity.ok("profile deleted " + authUser.getId());
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update from profile {} {}", USER_ENTITY_NAME, authUser.getEmail());
        assureIdConsistent(user, authUser.getId());
        user.setRoles(convertToRoles(authUser.getAuthorities()));
        userService.update(authUser.getId(), user);
        return ResponseEntity.ok("profile updated " + authUser.getId());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
