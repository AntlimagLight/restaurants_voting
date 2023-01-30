package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.security.AuthUser;
import com.topjava.restaurant_voting.service.UserService;
import com.topjava.restaurant_voting.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.VoteService.MAX_CHANGE_VOTE_TIME;
import static com.topjava.restaurant_voting.service.VoteService.VOTE_ENTITY_NAME;


@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection"})
@RestController
@Slf4j
@RequestMapping("/user/votes")
public class VoteController {
    @Autowired
    private VoteService voteService;
    @Autowired
    private UserService userService;

    @GetMapping("/{vote_date}")
    public ResponseEntity getVote(@AuthenticationPrincipal AuthUser authUser, @PathVariable LocalDate vote_date) {
        try {
            log.info("get " + VOTE_ENTITY_NAME + " by " + vote_date);
            return ResponseEntity.ok(voteService.getByDate(userService.getById(authUser.getId()), vote_date));
        } catch (NotExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PostMapping("")
    public ResponseEntity makeVote(@AuthenticationPrincipal AuthUser authUser, @RequestParam Integer restaurant_id) {
        try {
            log.info(USER_ENTITY_NAME + " make vote for " + RESTAURANT_ENTITY_NAME + " " + restaurant_id);
            voteService.makeVote(userService.getById(authUser.getId()), restaurant_id);
            return ResponseEntity.ok(VOTE_ENTITY_NAME + " saved");
        } catch (AlreadyExistException e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @PutMapping("")
    public ResponseEntity changeVote(@AuthenticationPrincipal AuthUser authUser, @RequestParam Integer restaurant_id) {
        try {
            log.info(USER_ENTITY_NAME + " change vote to " + RESTAURANT_ENTITY_NAME + " " + restaurant_id);
            boolean voteSuccess = voteService.changeVote(userService.getById(authUser.getId()), restaurant_id);
            if (voteSuccess) {
                return ResponseEntity.ok(VOTE_ENTITY_NAME + " changed");
            } else {
                return ResponseEntity.ok("Voting change time is out, you can change you vote between 0:00 and "
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

    @GetMapping
    public ResponseEntity getAllUserVotes(@AuthenticationPrincipal AuthUser authUser) {
        try {
            log.info("get all " + VOTE_ENTITY_NAME + " made by " + authUser.getId());
            return ResponseEntity.ok(voteService.getAllByUser(userService.getById(authUser.getId())));
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
