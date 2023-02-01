package com.topjava.restaurant_voting.dto;

import com.topjava.restaurant_voting.model.Vote;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VoteDto {

    private Long id;

    private Integer user_id;
    private Integer restaurant_id;
    @NotNull
    private LocalDate date;

    public VoteDto(Long id, Integer user_id, Integer restaurant_id, LocalDate date) {
        this.id = id;
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
        this.date = date;
    }

    public VoteDto(Vote vote) {
        this.id = vote.getId();
        this.user_id = vote.getUser().getId();
        this.restaurant_id = vote.getRestaurant().getId();
        this.date = vote.getDate();
    }

}