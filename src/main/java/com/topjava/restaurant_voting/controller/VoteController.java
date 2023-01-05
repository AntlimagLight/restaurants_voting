package com.topjava.restaurant_voting.controller;

import com.topjava.restaurant_voting.exeption.NotExistException;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.topjava.restaurant_voting.exeption.ExceptionMassages.BAD_REQUEST_MASSAGE;
import static com.topjava.restaurant_voting.service.VoteService.VOTE_ENTITY_NAME;
import static com.topjava.restaurant_voting.util.InitiateUtil.VOTING_END_TIME;

@SuppressWarnings({"rawtypes", "SpringJavaAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping("/user/restaurant")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/{restaurant_id}/vote")
    public ResponseEntity makeVote(@RequestParam Integer user_id, @PathVariable Integer restaurant_id) {
        try {
            Boolean voteSuccess = voteService.makeVote(userRepository.findById(user_id).get(), restaurant_id);
            // Тут использовать авторизацию
            if (voteSuccess) {
                return ResponseEntity.ok(VOTE_ENTITY_NAME + " saved:\n");
            } else {
                return ResponseEntity.ok("Voting time is out, you can vote between 0:00 and "
                        + VOTING_END_TIME.toString());
            }
        } catch (NotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

    @GetMapping("/statistic")
    public ResponseEntity viewStatistic (@RequestParam LocalDate date) {
        try {
            return ResponseEntity.ok(voteService.getStatistic(date));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BAD_REQUEST_MASSAGE);
        }
    }

}
