package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import com.topjava.restaurant_voting.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.InitiateUtil.MAX_CHANGE_VOTE_TIME;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class VoteService {
    private static final Logger log = LoggerFactory.getLogger(VoteService.class);
    public static final String VOTE_ENTITY_NAME = "Vote";
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public Boolean makeVote(User user, Integer restaurant_id) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Restaurant> opt = restaurantRepository.findById(restaurant_id);
        if (opt.isEmpty()) {
            throw new NotExistException(RESTAURANT_ENTITY_NAME);
        }
        Vote actualVote = voteRepository.findByUserAndDate(user, now.toLocalDate());
        if (actualVote == null) {
            log.debug("no user vote found for today");
            voteRepository.save(new Vote(null, user, opt.get(), now.toLocalDate()));
        } else {
            log.debug("user's vote for today found");
            if (now.toLocalTime().isBefore(MAX_CHANGE_VOTE_TIME)) {
                actualVote.setRestaurant(opt.get());
                voteRepository.save(actualVote);
            } else {
                log.info("The ability to change the voice is only available until" + MAX_CHANGE_VOTE_TIME);
                return false;
            }
        }
        return true;
    }

    public Map<String, Integer> getStatistic(LocalDate date) {
        List<Vote> voteList = voteRepository.findAllByDate(date);
        Map<String, Integer> result = new HashMap<>();
        for (Vote vote: voteList) {
            String restaurant_name = vote.getRestaurant().getName();
            if (result.containsKey(restaurant_name)) {
                //noinspection ConstantConditions
                result.compute(restaurant_name, (key, counter) -> counter + 1);
            } else {
                log.debug(RESTAURANT_ENTITY_NAME + " " + restaurant_name + " was found");
                result.put(restaurant_name, 1);
            }
        }
        return result;
    }
}