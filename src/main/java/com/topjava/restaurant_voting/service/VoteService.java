package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.dto.VoteDto;
import com.topjava.restaurant_voting.mapper.VoteMapper;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
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

@SuppressWarnings({"OptionalGetWithoutIsPresent"})
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {
    public static final String VOTE_ENTITY_NAME = "Vote";
    public static final LocalTime MAX_CHANGE_VOTE_TIME = LocalTime.of(11, 0);
    public static final VoteMapper voteMapper = Mappers.getMapper(VoteMapper.class);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public VoteDto getUsersVoteByDate(Integer user_id, LocalDate localDate) {
        User user = userRepository.findById(user_id).get();
        Optional<Vote> voteOpt = voteRepository.findByUserAndDate(user, localDate);
        assertExistence(voteOpt, VOTE_ENTITY_NAME);
        return voteMapper.toDTO(voteOpt.get());
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
            log.info("The ability to change the voice is only available until {}", MAX_CHANGE_VOTE_TIME);
            return false;
        }
    }

    public List<VoteDto> getAllByUser(Integer user_id) {
        User user = userRepository.findById(user_id).get();
        return voteRepository.findAllByUser(user).stream().map(voteMapper::toDTO).toList();
    }

    public Map<Integer, Integer> getStatistic(LocalDate date) {
        List<Vote> voteList = voteRepository.findAllByDate(date);
        Map<Integer, Integer> result = new HashMap<>();
        for (Vote vote : voteList) {
            Integer restaurant_id = vote.getRestaurant().getId();
            if (result.containsKey(restaurant_id)) {
                //noinspection ConstantConditions
                result.compute(restaurant_id, (key, counter) -> counter + 1);
            } else {
                log.debug("{} {} was found", RESTAURANT_ENTITY_NAME, restaurant_id);
                result.put(restaurant_id, 1);
            }
        }
        return result;
    }

}