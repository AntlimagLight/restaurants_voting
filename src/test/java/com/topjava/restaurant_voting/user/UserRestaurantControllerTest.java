package com.topjava.restaurant_voting.user;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import com.topjava.restaurant_voting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.topjava.restaurant_voting.utils_for_tests.TestData.ALL_RESTAURANTS;
import static com.topjava.restaurant_voting.utils_for_tests.TestData.REST_100004;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserRestaurantControllerTest extends RestaurantVotingApplicationTests {

    @Test
    void getRestaurant() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/user/restaurants/" + TESTING_RESTAURANT_ID)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(REST_100004),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void getNonExistent() throws Exception {
        this.mockMvc.perform(get("/user/restaurants/" + NOT_EXISTING_ID)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAll() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/user/restaurants")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(ALL_RESTAURANTS),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

}
