package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.dto.VoteDto;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import com.topjava.restaurant_voting.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertExistence;
import static com.topjava.restaurant_voting.util.ValidationUtils.assertNotExistence;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "OptionalGetWithoutIsPresent"})
@Service
@Transactional(readOnly = true)
public class VoteService {
    private static final Logger log = LoggerFactory.getLogger(VoteService.class);
    public static final String VOTE_ENTITY_NAME = "Vote";
    public static final LocalTime MAX_CHANGE_VOTE_TIME = LocalTime.of(11, 0);
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public VoteDto getByDate(User user, LocalDate localDate) {
        Optional<Vote> voteOpt = voteRepository.findByUserAndDate(user, localDate);
        assertExistence(voteOpt, VOTE_ENTITY_NAME);
        return new VoteDto(voteOpt.get());
    }

    @Transactional
    public void makeVote(User user, Integer restaurantId) {
        LocalDate now = LocalDateTime.now().toLocalDate();
        Restaurant restaurant = assertExistence(restaurantRepository.findById(restaurantId), RESTAURANT_ENTITY_NAME);
        assertNotExistence(voteRepository.findByUserAndDate(user, now),
                "today for this " + USER_ENTITY_NAME + " " + VOTE_ENTITY_NAME);
        voteRepository.save(new Vote(null, user, restaurant, now));
    }

    @Transactional
    public boolean changeVote(User user, Integer restaurantId) {
        LocalDateTime now = LocalDateTime.now();
        Restaurant restaurant = assertExistence(restaurantRepository.findById(restaurantId), RESTAURANT_ENTITY_NAME);
        Optional<Vote> actualVoteOpt = voteRepository.findByUserAndDate(user, now.toLocalDate());
        assertExistence(actualVoteOpt, "today for this " + USER_ENTITY_NAME + " " + VOTE_ENTITY_NAME);
        @SuppressWarnings("OptionalGetWithoutIsPresent") Vote actualVote = actualVoteOpt.get();
        if (now.toLocalTime().isBefore(MAX_CHANGE_VOTE_TIME)) {
            actualVote.setRestaurant(restaurant);
            voteRepository.save(actualVote);
            return true;
        } else {
            log.info("The ability to change the voice is only available until" + MAX_CHANGE_VOTE_TIME);
            return false;
        }
    }

    public List<VoteDto> getAllByUser(User user) {
        return voteRepository.findAllByUser(user).stream().map(VoteDto::new).toList();
    }

    public Map<String, Integer> getStatistic(LocalDate date) {
        List<Vote> voteList = voteRepository.findAllByDate(date);
        Map<String, Integer> result = new HashMap<>();
        for (Vote vote : voteList) {
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