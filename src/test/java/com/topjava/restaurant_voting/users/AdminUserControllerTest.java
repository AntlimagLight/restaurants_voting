package com.topjava.restaurant_voting.users;

import com.topjava.restaurant_voting.RestaurantVotingApplicationTests;
import com.topjava.restaurant_voting.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.topjava.restaurant_voting.test_utils.TestData.*;
import static com.topjava.restaurant_voting.util.InitiateDataUtil.USER2;
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
                        .content(jsonWithPassword(NEW_ADMIN, NEW_ADMIN.getPassword())))
                .andDo(print())
                .andExpect(status().isOk());
        User user = userRepository.findByEmail(NEW_ADMIN.getEmail()).get();
        USER_MATCHER.assertMatch(user, setIdForTests(NEW_ADMIN, user.getId()));
    }

    @Test
    void createAlreadyExist() throws Exception {
        this.mockMvc.perform(post("/admin/users")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(DUPLICATE_EMAIL_USER, DUPLICATE_EMAIL_USER.getPassword())))
                .andDo(print())
                .andExpect(status().is(400));
        assertFalse(userRepository.findById(NEW_ENTITY_ID).isPresent());
    }

    @Test
    void updateUser() throws Exception {
        this.mockMvc.perform(put("/admin/users/" + TESTING_USER_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(UPD_USER_TO_ADMIN, UPD_USER_TO_ADMIN.getPassword())))
                .andDo(print())
                .andExpect(status().isOk());
        User user = userRepository.findById(TESTING_USER_ID).get();
        USER_MATCHER.assertMatch(user, setIdForTests(UPD_USER_TO_ADMIN, user.getId()));
    }

    @Test
    void updateNonExistence() throws Exception {
        this.mockMvc.perform(put("/admin/users/" + NOT_EXISTING_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(UPD_USER_TO_ADMIN, UPD_USER_TO_ADMIN.getPassword())))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void SwitchEnabled() throws Exception {
        this.mockMvc.perform(put("/admin/users/" + TESTING_USER_ID + "/block")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk());
        assertNotSame(userRepository.findById(TESTING_USER_ID).get().getEnabled(), USER2.getEnabled());
    }

    @Test
    void SwitchEnabledNotAdmin() throws Exception {
        this.mockMvc.perform(put("/admin/users/" + TESTING_USER_ID + "/block")
                        .with(httpBasic(USER_LOGIN_EMAIL, USER_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(403));
        assertSame(userRepository.findById(TESTING_USER_ID).get().getEnabled(), USER2.getEnabled());
    }

    @Test
    void getUserById() throws Exception {
        this.mockMvc.perform(get("/admin/users/" + TESTING_USER_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER_100001));
    }

    @Test
    void getNonExistenceById() throws Exception {
        this.mockMvc.perform(get("/admin/users/" + NOT_EXISTING_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getByEmail() throws Exception {
        this.mockMvc.perform(get("/admin/users?email=" + USER_100001.getEmail())
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER_100001));
    }

    @Test
    void getNonExistenceByEmail() throws Exception {
        this.mockMvc.perform(get("/admin/users?email=nonexmail@mail.ru")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/admin/users/" + TESTING_USER_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(userRepository.findById(TESTING_USER_ID).isPresent());
    }

    @Test
    void deleteNonExistenceUser() throws Exception {
        this.mockMvc.perform(delete("/admin/users/" + NOT_EXISTING_ID)
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getAll() throws Exception {
        this.mockMvc.perform(get("/admin/users/list")
                        .with(httpBasic(ADMIN_LOGIN_EMAIL, ADMIN_LOGIN_PASSWORD)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ALL_USERS));
    }
}
