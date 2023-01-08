package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.VoteService.VOTE_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.InitiateUtil.MAX_CHANGE_VOTE_TIME;

@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping("/user/restaurant")
public class VoteController {
    private static final Logger log = LoggerFactory.getLogger(VoteController.class);
    @Autowired
    private VoteService voteService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/{restaurant_id}/vote")
    public ResponseEntity makeVote(@RequestParam Integer user_id, @PathVariable Integer restaurant_id) {
        try {
            log.info(USER_ENTITY_NAME + " make vote for " + RESTAURANT_ENTITY_NAME + " " + restaurant_id);
            Boolean voteSuccess = voteService.makeVote(userRepository.findById(user_id).get(), restaurant_id);
            // Тут использовать авторизацию
            if (voteSuccess) {
                return ResponseEntity.ok(VOTE_ENTITY_NAME + " saved:\n");
            } else {
                return ResponseEntity.ok("Voting time is out, you can vote between 0:00 and "
                        + MAX_CHANGE_VOTE_TIME.toString());
            }
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/statistic")
    public ResponseEntity viewStatistic(@RequestParam LocalDate date) {
        try {
            log.info("view statistics for the " + date);
            return ResponseEntity.ok(voteService.getStatistic(date));
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

}
