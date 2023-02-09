package com.topjava.restaurant_voting.controller.users;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.UserUtils.assureDefaultRole;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<URI> createUser(@Valid @RequestBody User user) {
        log.info("create {} {}", USER_ENTITY_NAME, user.getEmail());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/users/{id}")
                .buildAndExpand(userService.create(assureDefaultRole(user)).getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(uriOfNewResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@Valid @RequestBody User user, @PathVariable Integer id) {
        log.info("update {} {}", USER_ENTITY_NAME, user.getEmail());
        userService.update(id, assureDefaultRole(user));
        return ResponseEntity.status(204).body(USER_ENTITY_NAME + " updated:\n" + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> SwitchEnabled(@PathVariable Integer id) {
        log.info("switch enabled for {} {}", USER_ENTITY_NAME, id);
        userService.switchEnable(id);
        return ResponseEntity.status(204).body("'enable' switched for " + USER_ENTITY_NAME + " " + id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable Integer id) {
        log.info("get {} {}", USER_ENTITY_NAME, id);
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        log.info("get {} {}", USER_ENTITY_NAME, email);
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        log.info("delete {} {}", USER_ENTITY_NAME, id);
        userService.delete(id);
        return ResponseEntity.status(204).body(USER_ENTITY_NAME + " deleted:\n" + id);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        log.info("get all {}", USER_ENTITY_NAME);
        return ResponseEntity.ok(userService.getAll());
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
