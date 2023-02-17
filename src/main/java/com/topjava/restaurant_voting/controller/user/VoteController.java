package com.topjava.restaurant_voting.controller.user;

import com.topjava.restaurant_voting.dto.VoteCountDto;
import com.topjava.restaurant_voting.dto.VoteDto;
import com.topjava.restaurant_voting.security.AuthUser;
import com.topjava.restaurant_voting.service.UserService;
import com.topjava.restaurant_voting.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

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

    @Operation(
            summary = "Get Vote",
            description = "Gets the user's vote for the specified date."
    )
    @GetMapping("/{vote_date}")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<VoteDto> getVote(@AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable @Parameter(example = "2022-06-10") LocalDate vote_date) {
        log.info("get {} by {}", VOTE_ENTITY_NAME, vote_date);
        return ResponseEntity.ok(voteService.getUsersVoteByDate(authUser.getId(), vote_date));
    }

    @Operation(
            summary = "Make Vote",
            description = "The user votes today for the restaurant with {restaurant_id}."
    )
    @PostMapping()
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<URI> makeVote(@AuthenticationPrincipal AuthUser authUser,
                                        @RequestParam @Parameter(example = "100004") Long restaurant_id) {
        log.info("{} make vote for {} {}", USER_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/votes/{vote_date}")
                .buildAndExpand(voteService.makeVote(userService.getById(authUser.getId()),
                        restaurant_id).getDate()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(uriOfNewResource);
    }

    @Operation(
            summary = "Change Vote",
            description = "The user changes their today's vote to a restaurant with id {restaurant_id}." +
                    "<br>Two cases are possible:" +
                    "<br>- The time is less than 11:00 -> the previous vote for today is deleted, " +
                    "it is replaced by the vote for the restaurant with {restaurant_id}." +
                    "<br>- Time 11:00 and more -> the vote will simply not be counted."
    )
    @PutMapping()
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<String> changeVote(@AuthenticationPrincipal AuthUser authUser,
                                             @RequestParam @Parameter(example = "100004") Long restaurant_id) {
        log.info("{} change vote for {} {}", USER_ENTITY_NAME, RESTAURANT_ENTITY_NAME, restaurant_id);
        if (voteService.changeVote(userService.getById(authUser.getId()), restaurant_id)) {
            return ResponseEntity.status(204).body(null);
        } else {
            return ResponseEntity.status(422).body("Voting change time is out, you can change you vote between 0:00 and "
                    + MAX_CHANGE_VOTE_TIME.toString());
        }
    }

    @Operation(
            summary = "Get All User Votes",
            description = "Gets the full list of authorized user's votes."
    )
    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<VoteDto>> getAllUserVotes(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get all {} made by {}", VOTE_ENTITY_NAME, authUser.getId());
        return ResponseEntity.ok(voteService.getAllByUser(authUser.getId()));
    }

    @Operation(
            summary = "Get Statistic",
            description = "In response to the request, you get statistic. " +
                    "The calculation is relevant for the date specified in the parameters.<br>" +
                    "NOTE! You can see the actual statistics every 5 minutes. " +
                    "When you try to request statistics more often, the same latest version will view."
    )
    @GetMapping("/statistic")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<VoteCountDto>> viewStatistic(
            @RequestParam @Parameter(example = "2022-06-10") LocalDate date) {
        log.info("view statistics for the {}", date);
        return ResponseEntity.ok(voteService.getStatistic(date));
    }

}
