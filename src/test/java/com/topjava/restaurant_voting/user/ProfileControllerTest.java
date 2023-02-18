package com.topjava.restaurant_voting.user;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import com.topjava.restaurant_voting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.utils_for_tests.TestData.*;
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
        ResultActions resultActions = this.mockMvc.perform(get("/user/profile")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(USER_100000),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void deleteAuthUser() throws Exception {
        this.mockMvc.perform(delete("/user/profile")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(204));
        assertFalse(userRepository.findById(START_SEQ).isPresent());
    }

    @Test
    void updateAuthUser() throws Exception {
        this.mockMvc.perform(put("/user/profile")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(UPD_USER_DTO, UPD_USER_DTO.getPassword())))
                .andDo(print())
                .andExpect(status().is(204));
        USER_DTO_MATCHER.assertMatch(userMapper.toDTO(userRepository.findById(START_SEQ).get()),
                setIdForTests(UPD_USER_DTO, START_SEQ));
    }

}
