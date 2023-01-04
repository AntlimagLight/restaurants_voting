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
        if (LocalDateTime.now().toLocalTime().isBefore(VOTING_END_TIME)) {
            Optional<Restaurant> opt = restaurantRepository.findById(restaurant_id);
            if (opt.isEmpty()) {
                throw new NotExistException(RESTAURANT_ENTITY_NAME);
            }
            Vote actualVote = voteRepository.findByUser(user);
            if (actualVote == null) {
                voteRepository.save(new Vote(null, user, restaurant_id));
            } else {
                @SuppressWarnings("OptionalGetWithoutIsPresent")
                Restaurant oldRestaurant = restaurantRepository.findById(actualVote.getRestaurant_id()).get();
                oldRestaurant.setVoteCounter(oldRestaurant.getVoteCounter() - 1);
                restaurantRepository.save(oldRestaurant);
                actualVote.setRestaurant_id(restaurant_id);
                voteRepository.save(actualVote);
            }
            Restaurant restaurant = opt.get();
            restaurant.setVoteCounter(restaurant.getVoteCounter() + 1);
            restaurantRepository.save(restaurant);
            return true;
        } else {
//            Голосование уже завершено
            return false;
        }
    }
}