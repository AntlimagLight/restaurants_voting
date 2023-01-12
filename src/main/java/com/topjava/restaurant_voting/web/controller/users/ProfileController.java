package com.topjava.restaurant_voting.web.controller.users;

import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.service.UserService;
import com.topjava.restaurant_voting.web.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.UserUtils.assureIdConsistent;
import static com.topjava.restaurant_voting.util.UserUtils.convertToRoles;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "rawtypes"})
@RestController
@RequestMapping(value = "/user/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get from profile " + USER_ENTITY_NAME + " " + authUser.getEmail());
        return ResponseEntity.ok(userService.getById(authUser.getId()));
    }

    @DeleteMapping
    public ResponseEntity delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete from profile " + USER_ENTITY_NAME + " " + authUser.getEmail());
        return ResponseEntity.ok("profile deleted " + userService.delete(authUser.getId()));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update from profile " + USER_ENTITY_NAME + " " + authUser.getEmail());
        assureIdConsistent(user, authUser.getId());
        user.setRoles(convertToRoles(authUser.getAuthorities()));
        userService.update(authUser.getId(), user);
        return ResponseEntity.ok("profile updated");
    }
}
