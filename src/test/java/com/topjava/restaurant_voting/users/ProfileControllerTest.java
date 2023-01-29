package com.topjava.restaurant_voting.users;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.test_utils.TestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ProfileControllerTest extends RestaurantVotingApplicationTests {

    @Test
    void getAuthUser() throws Exception {
        this.mockMvc.perform(get("/user/profile")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER_100000));
    }

    @Test
    void deleteAuthUser() throws Exception {
        this.mockMvc.perform(delete("/user/profile")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(userRepository.findById(START_SEQ).isPresent());
    }

    @Test
    void updateAuthUser() throws Exception {
        this.mockMvc.perform(put("/user/profile")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(UPD_USER, UPD_USER.getPassword())))
                .andDo(print())
                .andExpect(status().isOk());
        USER_MATCHER.assertMatch(userRepository.findById(START_SEQ).get(), setIdForTests(UPD_USER, START_SEQ));
    }

}
