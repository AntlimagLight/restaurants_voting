package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.security.AuthUser;
import com.topjava.restaurant_voting.service.UserService;
import com.topjava.restaurant_voting.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.VoteService.MAX_CHANGE_VOTE_TIME;
import static com.topjava.restaurant_voting.service.VoteService.VOTE_ENTITY_NAME;


@SuppressWarnings({"rawtypes"})
@RestController
@Slf4j
@RequestMapping("/user/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;
    private final UserService userService;

    @GetMapping("/{vote_date}")
    public ResponseEntity getVote(@AuthenticationPrincipal AuthUser authUser, @PathVariable LocalDate vote_date) {
        try {
            log.info("get {} by {}", VOTE_ENTITY_NAME, vote_date);
            return ResponseEntity.ok(voteService.getUsersVoteByDate(authUser.getId(), vote_date));
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
            log.info("{} make vote for {} {}", USER_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
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
            log.info("{} change vote for {} {}", USER_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
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
            log.info("get all {} made by {}", VOTE_ENTITY_NAME, authUser.getId());
            return ResponseEntity.ok(voteService.getAllByUser(authUser.getId()));
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
            log.info("view statistics for the {}", date);
            return ResponseEntity.ok(voteService.getStatistic(date));
        } catch (Exception e) {
            log.warn(BAD_REQUEST_MASSAGE);
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    //TODO Make new Mappers for Json

}
