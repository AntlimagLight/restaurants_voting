package com.topjava.restaurant_voting.users;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import com.topjava.restaurant_voting.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.topjava.restaurant_voting.test_utils.TestData.*;
import static com.topjava.restaurant_voting.util.UserUtils.STARTING_ROLES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class RegisterControllerTest extends RestaurantVotingApplicationTests {

    @Test
    void registration() throws Exception {
        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(NEW_USER, NEW_USER.getPassword())))
                .andDo(print())
                .andExpect(status().is(201));
        User user = userRepository.findByEmail(NEW_USER.getEmail()).get();
        USER_MATCHER.assertMatch(user, setIdForTests(NEW_USER, user.getId()));
    }

    @Test
    void registrationDuplicate() throws Exception {
        User newUser = NEW_USER;
        newUser.setEmail(USER_100001.getEmail());
        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(newUser, NEW_USER.getPassword())))
                .andDo(print())
                .andExpect(status().is(422));
        assertFalse(userRepository.findById(NEW_ENTITY_ID).isPresent());
    }

    @Test
    void registrationAdmin() throws Exception {
        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(NEW_ADMIN, NEW_ADMIN.getPassword())))
                .andDo(print())
                .andExpect(status().is(201));
        assertEquals(userRepository.findByEmail(NEW_ADMIN.getEmail()).get().getRoles(), STARTING_ROLES);
    }
}
