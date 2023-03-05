package com.topjava.restaurant_voting.admin;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import com.topjava.restaurant_voting.dto.UserDto;
import com.topjava.restaurant_voting.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.topjava.restaurant_voting.util.InitiateDataUtil.USER2;
import static com.topjava.restaurant_voting.utils_for_tests.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AdminUserControllerTest extends RestaurantVotingApplicationTests {

    @Test
    void createUser() throws Exception {
        this.mockMvc.perform(post("/admin/users")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(NEW_ADMIN_DTO, NEW_ADMIN_DTO.getPassword())))
                .andDo(print())
                .andExpect(status().isCreated());
        UserDto user = sortRoles(userMapper.toDTO(userRepository.findByEmail(NEW_ADMIN.getEmail()).get()));
        USER_DTO_MATCHER.assertMatch(user, setIdForTests(NEW_ADMIN_DTO, user.getId()));
    }

    @Test
    void createAlreadyExist() throws Exception {
        this.mockMvc.perform(post("/admin/users")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(DUPLICATE_EMAIL_USER_DTO, DUPLICATE_EMAIL_USER_DTO.getPassword())))
                .andDo(print())
                .andExpect(status().is(422))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertFalse(userRepository.findById(NEW_ENTITY_ID).isPresent());
    }

    @Test
    void updateUser() throws Exception {
        this.mockMvc.perform(put("/admin/users/" + TESTING_USER_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(UPD_USER_TO_ADMIN_DTO, UPD_USER_TO_ADMIN_DTO.getPassword())))
                .andDo(print())
                .andExpect(status().is(204));
        UserDto user = sortRoles(userMapper.toDTO(userRepository.findById(TESTING_USER_ID).get()));
        USER_DTO_MATCHER.assertMatch(user, setIdForTests(UPD_USER_TO_ADMIN_DTO, user.getId()));
    }

    @Test
    void updateNonExistence() throws Exception {
        this.mockMvc.perform(put("/admin/users/" + NOT_EXISTING_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(UPD_USER_TO_ADMIN_DTO, UPD_USER_TO_ADMIN_DTO.getPassword())))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void switchEnabled() throws Exception {
        this.mockMvc.perform(patch("/admin/users/" + TESTING_USER_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(204));
        assertNotSame(userRepository.findById(TESTING_USER_ID).get().getEnabled(), USER2.getEnabled());
    }

    @Test
    void switchEnabledNotAdmin() throws Exception {
        this.mockMvc.perform(patch("/admin/users/" + TESTING_USER_ID)
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(403));
        assertSame(userRepository.findById(TESTING_USER_ID).get().getEnabled(), USER2.getEnabled());
    }

    @Test
    void getUserById() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/admin/users/" + TESTING_USER_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(USER_100001),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void getNonExistenceById() throws Exception {
        this.mockMvc.perform(get("/admin/users/" + NOT_EXISTING_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getByEmail() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/admin/users/by-email?email="
                        + USER_100001.getEmail())
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(USER_100001),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void getNonExistenceByEmail() throws Exception {
        this.mockMvc.perform(get("/admin/users/by-email?email=nonexmail@mail.ru")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/admin/users/" + TESTING_USER_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(204));
        assertFalse(userRepository.findById(TESTING_USER_ID).isPresent());
    }

    @Test
    void deleteNonExistenceUser() throws Exception {
        this.mockMvc.perform(delete("/admin/users/" + NOT_EXISTING_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllDefault() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/admin/users")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(PAGE_ALL_USERS),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void getAllWithPages() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/admin/users?pageNumber=0&pageSize=2")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        JSONAssert.assertEquals(JsonUtil.writeValue(PAGE_ALL_USERS_ON_FIRST_PAGE),
                resultActions.andReturn().getResponse().getContentAsString(), false);
    }

    @Test
    void getAllEmptyPage() throws Exception {
        this.mockMvc.perform(get("/admin/users?pageNumber=2&pageSize=2")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

}
