package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class UserService {
    public static final String USER_ENTITY_NAME = "User";
    @Autowired
    private UserRepository userRepository;

    public void create(User user) throws AlreadyExistException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new AlreadyExistException(USER_ENTITY_NAME + " with this email");
        }
        user.setEnabled(true);
        LocalDateTime now = LocalDateTime.now();
        user.setRegistered(now.toLocalDate());
        userRepository.save(user);
    }

    public void update(Integer id, User user) throws NotExistException {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            throw new NotExistException(USER_ENTITY_NAME);
        }
        user.setId(id);
        User oldUser = opt.get();
        user.setRegistered(oldUser.getRegistered());
        user.setVote(oldUser.getVote());
        userRepository.save(user);
    }

    public UserTo getById(Integer id) throws NotExistException {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            throw new NotExistException(USER_ENTITY_NAME);
        }
        return new UserTo(opt.get());
    }

    public UserTo getByEmail(String email) throws NotExistException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotExistException(USER_ENTITY_NAME);
        }
        return new UserTo(user);
    }

    public Integer delete(int id) throws NotExistException {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotExistException(USER_ENTITY_NAME);
        }
        userRepository.deleteById(id);
        return id;
    }

    public List<UserTo> getAll() {
        List<User> srcList = (List<User>) userRepository.findAll();
        return srcList.stream()
                .map(UserTo::new)
                .collect(Collectors.toList());
    }

}