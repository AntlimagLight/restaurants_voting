package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

    @Transactional
    public User create(User user) throws AlreadyExistException {
        assertNotExistence(userRepository.findByEmail(user.getEmail()), USER_ENTITY_NAME + " with this email");
        user.setEnabled(true);
        LocalDateTime now = LocalDateTime.now();
        user.setRegistered(now.toLocalDate());
        return userRepository.save(UserUtils.prepareToSave(user));
    }

    @Transactional
    public void update(Long id, User user) throws NotExistException {
        User oldUser = assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        user.setId(id);
        user.setRegistered(oldUser.getRegistered());
        user.setEnabled(oldUser.getEnabled());
        userRepository.save(UserUtils.prepareToSave(user));
    }

    @Transactional
    public void switchEnable(Long id) throws NotExistException {
        User user = assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    @Cacheable(cacheNames = "userCache", key = "#id")
    public User getById(Long id) throws NotExistException {
        return assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
    }

    public User getByEmail(String email) throws NotExistException {
        return assertExistence(userRepository.findByEmail(email), USER_ENTITY_NAME);
    }

    @Transactional
    public void delete(long id) throws NotExistException {
        assertExistence(userRepository.findById(id), USER_ENTITY_NAME);
        userRepository.deleteById(id);
    }

    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

}