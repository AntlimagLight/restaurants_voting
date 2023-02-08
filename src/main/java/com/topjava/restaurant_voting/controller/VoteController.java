package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.dto.VoteDto;
import com.topjava.restaurant_voting.exeption.ResponseError;
import com.topjava.restaurant_voting.security.AuthUser;
import com.topjava.restaurant_voting.service.UserService;
import com.topjava.restaurant_voting.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.topjava.restaurant_voting.service.RestaurantService.RESTAURANT_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.UserService.USER_ENTITY_NAME;
import static com.topjava.restaurant_voting.service.VoteService.MAX_CHANGE_VOTE_TIME;
import static com.topjava.restaurant_voting.service.VoteService.VOTE_ENTITY_NAME;


@RestController
@Slf4j
@RequestMapping("/user/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;
    private final UserService userService;

    @GetMapping("/{vote_date}")
    public ResponseEntity<VoteDto> getVote(@AuthenticationPrincipal AuthUser authUser, @PathVariable LocalDate vote_date) {
        log.info("get {} by {}", VOTE_ENTITY_NAME, vote_date);
        return ResponseEntity.ok(voteService.getUsersVoteByDate(authUser.getId(), vote_date));
    }

    @PostMapping()
    public ResponseEntity<String> makeVote(@AuthenticationPrincipal AuthUser authUser, @RequestParam Integer restaurant_id) {
        log.info("{} make vote for {} {}", USER_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
        voteService.makeVote(userService.getById(authUser.getId()), restaurant_id);
        return ResponseEntity.ok(VOTE_ENTITY_NAME + " saved");
    }

    @PutMapping()
    public ResponseEntity<String> changeVote(@AuthenticationPrincipal AuthUser authUser, @RequestParam Integer restaurant_id) {
        log.info("{} change vote for {} {}", USER_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
        boolean voteSuccess = voteService.changeVote(userService.getById(authUser.getId()), restaurant_id);
        if (voteSuccess) {
            return ResponseEntity.ok(VOTE_ENTITY_NAME + " changed");
        } else {
            return ResponseEntity.ok("Voting change time is out, you can change you vote between 0:00 and "
                    + MAX_CHANGE_VOTE_TIME.toString());
        }
    }

    @GetMapping
    public ResponseEntity<List<VoteDto>> getAllUserVotes(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get all {} made by {}", VOTE_ENTITY_NAME, authUser.getId());
        return ResponseEntity.ok(voteService.getAllByUser(authUser.getId()));
    }

    @GetMapping("/statistic")
    public ResponseEntity<Map<Integer, Integer>> viewStatistic(@RequestParam LocalDate date) {
        log.info("view statistics for the {}", date);
        return ResponseEntity.ok(voteService.getStatistic(date));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
