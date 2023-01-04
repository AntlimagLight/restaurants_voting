package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;

@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {
        try {
            userService.create(user);
            return ResponseEntity.ok(USER_ENTITY_NAME + " saved:\n" + user);
        } catch (AlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody User user, @PathVariable Integer id) {
        try {
            userService.update(id, user);
            return ResponseEntity.ok(USER_ENTITY_NAME + " updated:\n" + user);
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserByID(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping
    public ResponseEntity getUserByEmail(@RequestParam String email) {
        try {
            return ResponseEntity.ok(userService.getByEmail(email));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(USER_ENTITY_NAME + " deleted:\n" + userService.delete(id));
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/list")
    public ResponseEntity getAll() {
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}
