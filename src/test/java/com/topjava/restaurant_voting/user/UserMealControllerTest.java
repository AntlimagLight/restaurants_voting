package com.topjava.restaurant_voting.user;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import com.topjava.restaurant_voting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.utils_for_tests.TestData.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserMealControllerTest extends RestaurantVotingApplicationTests {

    @Test
    void getMeal() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/user/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + TESTING_MEAL_ID)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(MEAL_100009),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void getAnotherRestaurantMeal() throws Exception {
        this.mockMvc.perform(get("/user/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + START_SEQ + 6)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getRestaurantMenu() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/user/restaurants/" + TESTING_RESTAURANT_ID + "/menu")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(ALL_MEALS_FROM_REST_100004),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void getTodayMenu() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/user/restaurants/today_menu")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(ALL_REST_MENU),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

}
