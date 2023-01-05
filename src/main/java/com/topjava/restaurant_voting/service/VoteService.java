package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import com.topjava.restaurant_voting.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.InitiateUtil.VOTING_END_TIME;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class VoteService {
    public static final String VOTE_ENTITY_NAME = "Vote";
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public Boolean makeVote(User user, Integer restaurant_id) {
        LocalDateTime now = LocalDateTime.now();
        if (now.toLocalTime().isBefore(VOTING_END_TIME)) {
            Optional<Restaurant> opt = restaurantRepository.findById(restaurant_id);
            if (opt.isEmpty()) {
                throw new NotExistException(RESTAURANT_ENTITY_NAME);
            }
            Vote actualVote = voteRepository.findByUserAndDate(user, now.toLocalDate());
            if (actualVote == null) {
                voteRepository.save(new Vote(null, user, opt.get(), now.toLocalDate()));
            } else {
                actualVote.setRestaurant(opt.get());
                voteRepository.save(actualVote);
            }
            return true;
        } else {
//            Голосование уже завершено сегодня
            return false;
        }
    }
}