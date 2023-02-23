package com.topjava.restaurant_voting.controller.admin;

import com.topjava.restaurant_voting.dto.UserDto;
import com.topjava.restaurant_voting.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.UserService.userMapper;
import static com.topjava.restaurant_voting.util.UserUtils.assureDefaultRole;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    @Operation(
            summary = "Create User",
            description = "Creates a user and adds it to the database. " +
                    "You can specify user roles. If no roles are specified, the USER role will be set by default."
    )
    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        log.info("create {} {}", USER_ENTITY_NAME, user.getEmail());
        val created = userService.create(assureDefaultRole(user));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(userMapper.toDTO(created));
    }

    @Operation(
            summary = "Update User",
            description = "Updates a user with ID it to the database. You can specify user roles." +
                    " If no roles are specified, the USER role will be set by default."
    )
    @PutMapping("/{id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserDto user,
                                             @PathVariable @Parameter(example = "100001") Long id) {
        log.info("update {} {}", USER_ENTITY_NAME, user.getEmail());
        userService.update(id, assureDefaultRole(user));
        return ResponseEntity.status(204).body(null);
    }

    @Operation(
            summary = "Block/Unblock User",
            description = "Blocks access to the application for the user with {id}. " +
                    "If the user is already blocked, unblocks him."
    )
    @PatchMapping("/{id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> SwitchEnabled(@PathVariable @Parameter(example = "100001") Long id) {
        log.info("switch enabled for {} {}", USER_ENTITY_NAME, id);
        userService.switchEnable(id);
        return ResponseEntity.status(204).body(null);
    }

    @Operation(
            summary = "Find User By ID",
            description = "In response, the data of user with {id} is received."
    )
    @GetMapping("/{id}")
    @SecurityRequirement(name = "basicAuth")
    public UserDto getUserByID(@PathVariable @Parameter(example = "100001") Long id) {
        log.info("get {} {}", USER_ENTITY_NAME, id);
        return userService.getById(id);
    }

    @Operation(
            summary = "Find User By Email",
            description = "In response, the user's data comes with the email specified in the param."
    )
    @GetMapping("/by-email")
    @SecurityRequirement(name = "basicAuth")
    public UserDto getUserByEmail(@RequestParam @Parameter(example = "user@yandex.ru") String email) {
        log.info("get {} {}", USER_ENTITY_NAME, email);
        return userService.getByEmail(email);
    }

    @Operation(
            summary = "Delete User",
            description = "Removes the user with {id} from the database."
    )
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> deleteUser(@PathVariable @Parameter(example = "100001") Long id) {
        log.info("delete {} {}", USER_ENTITY_NAME, id);
        userService.delete(id);
        return ResponseEntity.status(204).body(null);
    }

    @Operation(
            summary = "Get All Users",
            description = "The response is a list of all users."
    )
    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    public List<UserDto> getAll() {
        log.info("get all {}", USER_ENTITY_NAME);
        return userService.getAll();
    }

}
