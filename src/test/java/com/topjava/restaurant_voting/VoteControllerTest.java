package com.topjava.restaurant_voting;

import com.topjava.restaurant_voting.model.Vote;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.service.VoteService.MAX_CHANGE_VOTE_TIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class VoteControllerTest extends RestaurantVotingApplicationTests {


//    @Test
//    void getVote() throws Exception {
//        this.mockMvc.perform(get("/user/votes/2022-06-10")
//                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(VOTE_DTO_MATCHER.contentJson(VOTE_DTO_2));
//    }

    @Test
    void getNonExistent() throws Exception {
        this.mockMvc.perform(get("/user/votes/2010-10-10")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    void makeVote() throws Exception {
        this.mockMvc.perform(post("/user/votes?restaurant_id=" + TESTING_RESTAURANT_ID)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isCreated());
        assertEquals(voteRepository.findByUserAndDate(userRepository.findById(START_SEQ).get(),
                LocalDateTime.now().toLocalDate()).get().getRestaurant().getId(), TESTING_RESTAURANT_ID);
    }

    @Test
    void changeVote() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        int expectingStatus;
        if (now.toLocalTime().isBefore(MAX_CHANGE_VOTE_TIME)) {
            expectingStatus = 204;
        } else {
            expectingStatus = 422;
        }
        this.mockMvc.perform(put("/user/votes?restaurant_id=" + TESTING_RESTAURANT_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(expectingStatus));
        Vote vote = voteRepository.findByUserAndDate(userRepository.findById(START_SEQ + 2).get(), now.toLocalDate()).get();
        assertFalse(voteRepository.findById(100019L).isPresent());
        if (now.toLocalTime().isBefore(MAX_CHANGE_VOTE_TIME)) {
            assertEquals(vote.getRestaurant().getId(), TESTING_RESTAURANT_ID);
        } else {
            assertEquals(vote.getRestaurant().getId(), 100003L);
        }
    }

    @Test
    void makeVoteUnauthorized() throws Exception {
        this.mockMvc.perform(post("/user/votes/" + TESTING_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().is(401));
        assertFalse(voteRepository.findById(100019L).isPresent());
    }

//    @Test
//    void getAllUserVotes() throws Exception {
//        this.mockMvc.perform(get("/user/restaurants")
//                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(VOTE_DTO_MATCHER.contentJson(VOTES_BY_USER100000));
//    }

//    @SuppressWarnings("rawtypes")
//    @Test
//    void getStatistic() throws Exception {
//        this.mockMvc.perform(get("/user/votes/statistic?date=2022-06-10")
//                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MAP_MATCHER.contentJson((HashMap) STATISTIC));
//    }
}
