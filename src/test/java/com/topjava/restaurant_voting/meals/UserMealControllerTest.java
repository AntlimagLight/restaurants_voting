package com.topjava.restaurant_voting.meals;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.testutils.TestData.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserMealControllerTest extends RestaurantVotingApplicationTests {

    @Test
    void getMeal() throws Exception {
        this.mockMvc.perform(get("/user/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + TESTING_MEAL_ID)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(MEAL_100009));
    }

    @Test
    void getAnotherRestaurantMeal() throws Exception {
        this.mockMvc.perform(get("/user/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + START_SEQ + 6)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getAll() throws Exception {
        this.mockMvc.perform(get("/user/restaurants/" + TESTING_RESTAURANT_ID + "/menu")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(ALL_MEALS_FROM_REST_100004));
    }

}
