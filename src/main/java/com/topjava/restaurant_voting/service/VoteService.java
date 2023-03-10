package com.topjava.restaurant_voting.service;

import com.topjava.restaurant_voting.dto.VoteCountDto;
import com.topjava.restaurant_voting.dto.VoteDto;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.mapper.VoteMapper;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.Vote;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private final VoteMapper voteMapper;
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;


    public VoteDto getUsersVoteByDate(Long user_id, LocalDate localDate) {
        val user = userRepository.getReferenceById(user_id);
        val voteOpt = voteRepository.findByUserAndDate(user, localDate);
        return voteMapper.toDTO(assertExistence(voteOpt, VOTE_ENTITY_NAME));
    }

    @CacheEvict(value = "votesList", key = "#userId")
    @Transactional
    public Vote makeVote(Long userId, Long restaurantId) {
        val now = LocalDateTime.now().toLocalDate();
        val user = userRepository.getReferenceById(userId);
        val restaurant = getRestaurantReferenceIfExist(restaurantId);
        assertNotExistence(voteRepository.findByUserAndDate(user, now),
                "today for this " + USER_ENTITY_NAME + " " + VOTE_ENTITY_NAME);
        return voteRepository.save(new Vote(null, user, restaurant, now));
    }

    @CacheEvict(value = "votesList", key = "#userId")
    @Transactional
    public boolean changeVote(Long userId, Long restaurantId) {
        val now = LocalDateTime.now();
        val restaurant = getRestaurantReferenceIfExist(restaurantId);
        val actualVoteOpt = voteRepository.findByUserAndDate(userRepository.getReferenceById(userId), now.toLocalDate());
        Vote actualVote = assertExistence(actualVoteOpt, "today for this " + USER_ENTITY_NAME + " " + VOTE_ENTITY_NAME);
        if (now.toLocalTime().isBefore(MAX_CHANGE_VOTE_TIME)) {
            actualVote.setRestaurant(restaurant);
            voteRepository.save(actualVote);
            return true;
        } else {
            log.info("The ability to change the voice is only available until {}", MAX_CHANGE_VOTE_TIME);
            return false;
        }
    }

    @Cacheable(value = "votesList", key = "#user_id")
    public List<VoteDto> getAllByUser(Long user_id) {
        val user = userRepository.findById(user_id).get();
        return voteRepository.findAllByUser(user).stream().map(voteMapper::toDTO).toList();
    }

    @Cacheable(value = "stats", key = "#date")
    public List<VoteCountDto> getStatistics(LocalDate date) {
        return voteRepository.findAllByDate(date);
    }

    // All Statistic cache cleared every 5 minutes
    @CacheEvict(value = "stats", allEntries = true)
    @Scheduled(fixedDelayString = "${cache.timeout.clear-stats}")
    public void clearStatsCache() {
        log.debug("Statistic cache was cleared");
    }

    private Restaurant getRestaurantReferenceIfExist(Long restaurant_id) {
        if (!restaurantRepository.existsById(restaurant_id)) {
            throw new NotExistException(RESTAURANT_ENTITY_NAME);
        } else {
            return restaurantRepository.getReferenceById(restaurant_id);
        }
    }
}