package com.topjava.restaurant_voting;

import com.topjava.restaurant_voting.repository.MealRepository;
import com.topjava.restaurant_voting.repository.RestaurantRepository;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.topjava.restaurant_voting.model.AbstractBaseEntity.START_SEQ;
import static com.topjava.restaurant_voting.util.InitiateDataUtil.ADMIN;
import static com.topjava.restaurant_voting.util.InitiateDataUtil.USER1;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RestaurantVotingApplicationTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected MealRepository mealRepository;
    @Autowired
    protected RestaurantRepository restaurantRepository;
    @Autowired
    protected VoteRepository voteRepository;

    protected final static Long NOT_EXISTING_ID = START_SEQ - 1;
    protected final static Long TESTING_USER_ID = START_SEQ + 1;
    protected final static Long TESTING_RESTAURANT_ID = START_SEQ + 4;
    protected final static Long TESTING_MEAL_ID = START_SEQ + 9;
    protected final static Long NEW_ENTITY_ID = START_SEQ + 20;
    protected final static String USER_LOGIN_EMAIL = USER1.getEmail();
    protected final static String ADMIN_LOGIN_EMAIL = ADMIN.getEmail();
    protected final static String USER_LOGIN_PASSWORD = USER1.getPassword();
    protected final static String ADMIN_LOGIN_PASSWORD = ADMIN.getPassword();

}
