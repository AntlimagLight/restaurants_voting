package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.dto.UserDto;
import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.mapper.UserMapper;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public User create(UserDto user) throws AlreadyExistException {
        assertNotExistence(userRepository.findByEmail(user.getEmail()), USER_ENTITY_NAME + " with this email");
        User entity = userMapper.toModel(user);
        entity.setEnabled(true);
        entity.setRegistered(LocalDateTime.now().toLocalDate());
        return userRepository.save(UserUtils.prepareToSave(entity));
    }

    @Transactional
    public void update(Long id, UserDto user) throws NotExistException {
        val oldUser = assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        User entity = userMapper.toModel(user);
        entity.setId(id);
        entity.setRegistered(oldUser.getRegistered());
        entity.setEnabled(oldUser.getEnabled());
        userRepository.save(UserUtils.prepareToSave(entity));
    }

    @Transactional
    public void switchEnable(Long id) throws NotExistException {
        User user = assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        user.setEnabled(!user.getEnabled());
    }

    public UserDto getById(Long id) throws NotExistException {
        return userMapper.toDTO(assertExistence(userRepository.findById(id), USER_ENTITY_NAME));
    }

    public UserDto getByEmail(String email) throws NotExistException {
        return userMapper.toDTO(assertExistence(userRepository.findByEmail(email), USER_ENTITY_NAME));
    }

    @Transactional
    public void delete(long id) throws NotExistException {
        assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        userRepository.deleteById(id);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

}