package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.dto.UserDto;
import com.topjava.restaurant_voting.mapper.UserMapper;
import com.topjava.restaurant_voting.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private final UserMapper userMapper;

    @Operation(
            summary = "User registration",
            description = "Registers a user and adds it to the database. " +
                    "The user will automatically be assigned the USER role."
    )
    @PostMapping()
    public ResponseEntity<UserDto> registration(@Valid @RequestBody UserDto user) {
        log.info("registration {} {}", USER_ENTITY_NAME, user.getEmail());
        user.setRoles(STARTING_ROLES);
        val created = userService.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(userMapper.toDTO(created));
    }

}
