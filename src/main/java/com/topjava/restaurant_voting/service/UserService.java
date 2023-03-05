package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.dto.PageOfUsersDto;
import com.topjava.restaurant_voting.dto.UserDto;
import com.topjava.restaurant_voting.mapper.UserMapper;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.topjava.restaurant_voting.util.ValidationUtils.assertExistence;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertNotExistence;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    public static final String USER_ENTITY_NAME = "User";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User create(UserDto user) {
        assertNotExistence(userRepository.findByEmail(user.getEmail()), USER_ENTITY_NAME + " with this email");
        User entity = userMapper.toModel(user);
        entity.setEnabled(true);
        entity.setRegistered(LocalDateTime.now().toLocalDate());
        return userRepository.save(UserUtils.prepareToSave(entity));
    }

    @Transactional
    public void update(Long id, UserDto user) {
        val oldUser = assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        User entity = userMapper.toModel(user);
        entity.setId(id);
        entity.setRegistered(oldUser.getRegistered());
        entity.setEnabled(oldUser.getEnabled());
        userRepository.save(UserUtils.prepareToSave(entity));
    }

    @Transactional
    public void switchEnable(Long id) {
        User user = assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        user.setEnabled(!user.getEnabled());
    }

    public UserDto getById(Long id) {
        return userMapper.toDTO(assertExistence(userRepository.findById(id), USER_ENTITY_NAME));
    }

    public UserDto getByEmail(String email) {
        return userMapper.toDTO(assertExistence(userRepository.findByEmail(email), USER_ENTITY_NAME));
    }

    @Transactional
    public void delete(long id) {
        assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        userRepository.deleteById(id);
    }

    public PageOfUsersDto getAll(Pageable pageable) {
        val page = userRepository.findAll(pageable).map(userMapper::toDTO);
        if (page.getContent().isEmpty()) {
            throw new IllegalArgumentException("Blank page requested");
        }
        return new PageOfUsersDto(page.getTotalPages(), page.getTotalElements(), page.getSize(), page.getNumber(),
                page.getContent());
    }

}