package com.topjava.restaurant_voting.controller.user;

import com.topjava.restaurant_voting.dto.UserDto;
import com.topjava.restaurant_voting.security.AuthUser;
import com.topjava.restaurant_voting.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(
            summary = "Get User Profile",
            description = "In response, the data of the authorized user is received."
    )
    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<UserDto> get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get from profile {} {}", USER_ENTITY_NAME, authUser.getEmail());
        return ResponseEntity.ok(userService.getById(authUser.getId()));
    }

    @Operation(
            summary = "Delete User Profile",
            description = "Removes an authorized user from the database."
    )
    @DeleteMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete from profile {} {}", USER_ENTITY_NAME, authUser.getEmail());
        userService.delete(authUser.getId());
        return ResponseEntity.status(204).body(null);
    }

    @Operation(
            summary = "Update User Profile",
            description = "Changes the data of an authorized user in the database."
    )
    @PutMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> update(@Valid @RequestBody UserDto user, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update from profile {} {}", USER_ENTITY_NAME, authUser.getEmail());
        assureIdConsistent(user, authUser.getId());
        user.setRoles(convertToRoles(authUser.getAuthorities()));
        userService.update(authUser.getId(), user);
        return ResponseEntity.status(204).body(null);
    }

}
