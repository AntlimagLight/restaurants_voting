package com.topjava.restaurant_voting.admin;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import com.topjava.restaurant_voting.dto.MealDto;
import com.topjava.restaurant_voting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.utils_for_tests.TestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AdminMealControllerTest extends RestaurantVotingApplicationTests {

    @Test
    void createMeal() throws Exception {
        this.mockMvc.perform(post("/admin/restaurants/" + TESTING_RESTAURANT_ID + "/menu")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NEW_MEAL_DTO)))
                .andDo(print())
                .andExpect(status().isCreated());
        MealDto meal = mealMapper.toDTO(mealRepository
                .findByRestaurantAndName(restaurantRepository
                        .findById(TESTING_RESTAURANT_ID).get(), NEW_MEAL_DTO.getName()).get());
        MEAL_DTO_MATCHER.assertMatch(meal, setIdForTests(NEW_MEAL_DTO, meal.getId()));
    }

    @Test
    void createAlreadyExist() throws Exception {
        this.mockMvc.perform(post("/admin/restaurants/" + TESTING_RESTAURANT_ID + "/menu")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NEW_MEAL_ALREADY_EXIST_DTO)))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertFalse(mealRepository.findById(NEW_ENTITY_ID).isPresent());
    }

    @Test
    void updateMeal() throws Exception {
        this.mockMvc.perform(put("/admin/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + TESTING_MEAL_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(UPDATED_MEAL_DTO)))
                .andDo(print())
                .andExpect(status().is(204));
        MealDto meal = mealMapper.toDTO(mealRepository.findById(TESTING_MEAL_ID).get());
        MEAL_DTO_MATCHER.assertMatch(meal, setIdForTests(UPDATED_MEAL_DTO, meal.getId()));
    }

    @Test
    void updateAnotherRestaurantMeal() throws Exception {
        this.mockMvc.perform(put("/admin/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + START_SEQ + 6)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(UPDATED_MEAL_DTO)))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MealDto meal = mealMapper.toDTO(mealRepository.findById(START_SEQ + 6).get());
        MEAL_DTO_MATCHER.assertMatch(meal, MEAL_100006);
    }

    @Test
    void deleteMeal() throws Exception {
        this.mockMvc.perform(delete("/admin/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + TESTING_MEAL_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(204));
        assertFalse(mealRepository.findById(TESTING_MEAL_ID).isPresent());
    }

    @Test
    void deleteAnotherRestaurantMeal() throws Exception {
        this.mockMvc.perform(delete("/admin/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + START_SEQ + 6)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertTrue(mealRepository.findById(START_SEQ + 6).isPresent());
    }

    @Test
    void deleteNotAdmin() throws Exception {
        this.mockMvc.perform(delete("/admin/restaurants/" + TESTING_RESTAURANT_ID + "/menu/" + TESTING_MEAL_ID)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(403));
        assertTrue(mealRepository.findById(TESTING_MEAL_ID).isPresent());
    }

}
