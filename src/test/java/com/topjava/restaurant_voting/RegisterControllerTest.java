package com.topjava.restaurant_voting;

import com.topjava.restaurant_voting.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.topjava.restaurant_voting.util.UserUtils.STARTING_ROLES;
import static com.topjava.restaurant_voting.utils_for_tests.TestData.*;
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
                        .content(jsonWithPassword(NEW_USER_DTO, NEW_USER_DTO.getPassword())))
                .andDo(print())
                .andExpect(status().is(201));
        UserDto user = userMapper.toDTO(userRepository.findByEmail(NEW_USER_DTO.getEmail()).get());
        USER_DTO_MATCHER.assertMatch(user, setIdForTests(NEW_USER_DTO, user.getId()));
    }

    @Test
    void registrationDuplicate() throws Exception {
        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(DUPLICATE_EMAIL_USER_DTO, DUPLICATE_EMAIL_USER_DTO.getPassword())))
                .andDo(print())
                .andExpect(status().is(422));
        assertFalse(userRepository.findById(NEW_ENTITY_ID).isPresent());
    }

    @Test
    void registrationAdmin() throws Exception {
        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(NEW_ADMIN_DTO, NEW_ADMIN_DTO.getPassword())))
                .andDo(print())
                .andExpect(status().is(201));
        assertEquals(userRepository.findByEmail(NEW_ADMIN.getEmail()).get().getRoles(), STARTING_ROLES);
    }
}
