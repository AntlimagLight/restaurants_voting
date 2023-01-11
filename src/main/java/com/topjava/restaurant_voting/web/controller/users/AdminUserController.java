package com.topjava.restaurant_voting.web.controller.users;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;

@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection", "DuplicatedCode"})
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {
        try {
            log.info("create " + USER_ENTITY_NAME + " " + user.getEmail());
            userService.create(user);
            return ResponseEntity.ok(USER_ENTITY_NAME + " saved:\n" + user.getEmail());
        } catch (AlreadyExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody User user, @PathVariable Integer id) {
        try {
            log.info("update " + USER_ENTITY_NAME + " " + user.getEmail());
            userService.update(id, user);
            return ResponseEntity.ok(USER_ENTITY_NAME + " updated:\n" + user.getEmail());
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity SwitchEnabled(@PathVariable Integer id) {
        try {
            log.info("switch enabled for " + USER_ENTITY_NAME + " " + id);
            userService.switchEnable(id);
            return ResponseEntity.ok("'enable' switched for " + USER_ENTITY_NAME + " " + id);
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserByID(@PathVariable Integer id) {
        try {
            log.info("get " + USER_ENTITY_NAME + " " + id);
            return ResponseEntity.ok(userService.getById(id));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping
    public ResponseEntity getUserByEmail(@RequestParam String email) {
        try {
            log.info("get " + USER_ENTITY_NAME + " " + email);
            return ResponseEntity.ok(userService.getByEmail(email));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        try {
            log.info("delete " + USER_ENTITY_NAME + " " + id);
            return ResponseEntity.ok(USER_ENTITY_NAME + " deleted:\n" + userService.delete(id));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/list")
    public ResponseEntity getAll() {
        try {
            log.info("get all " + USER_ENTITY_NAME);
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }
}
