package com.topjava.restaurant_voting.admin;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.topjava.restaurant_voting.util.InitiateDataUtil.REST_2;
import static com.topjava.restaurant_voting.utils_for_tests.TestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AdminRestaurantControllerTest extends RestaurantVotingApplicationTests {

    @Test
    void createRestaurant() throws Exception {
        this.mockMvc.perform(post("/admin/restaurants")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NEW_RESTAURANT)))
                .andDo(print())
                .andExpect(status().is(201));
        Restaurant restaurant = restaurantRepository.findByName(NEW_RESTAURANT.getName()).get();
        RESTAURANT_MATCHER.assertMatch(restaurant, setIdForTests(NEW_RESTAURANT, restaurant.getId()));
    }

    @Test
    void createAlreadyExist() throws Exception {
        this.mockMvc.perform(post("/admin/restaurants")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(REST_2)))
                .andDo(print())
                .andExpect(status().is(422));
        assertFalse(restaurantRepository.findById(NEW_ENTITY_ID).isPresent());
    }

    @Test
    void updateRestaurant() throws Exception {
        this.mockMvc.perform(put("/admin/restaurants/" + TESTING_RESTAURANT_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(UPDATED_RESTAURANT)))
                .andDo(print())
                .andExpect(status().is(204));
        Restaurant restaurant = restaurantRepository.findById(TESTING_RESTAURANT_ID).get();
        RESTAURANT_MATCHER.assertMatch(restaurant, setIdForTests(UPDATED_RESTAURANT, restaurant.getId()));
    }

    @Test
    void updateNonExistence() throws Exception {
        this.mockMvc.perform(put("/admin/restaurants/" + NOT_EXISTING_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(UPDATED_RESTAURANT)))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    void deleteRestaurant() throws Exception {
        this.mockMvc.perform(delete("/admin/restaurants/" + TESTING_RESTAURANT_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(204));
        assertFalse(restaurantRepository.findById(TESTING_RESTAURANT_ID).isPresent());
    }

    @Test
    void deleteNonExistence() throws Exception {
        this.mockMvc.perform(delete("/admin/restaurants/" + NOT_EXISTING_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    void deleteNotAdmin() throws Exception {
        this.mockMvc.perform(delete("/admin/restaurants/" + TESTING_RESTAURANT_ID)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(403));
        assertTrue(restaurantRepository.findById(TESTING_RESTAURANT_ID).isPresent());
    }


}
