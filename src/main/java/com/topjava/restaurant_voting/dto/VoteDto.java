package com.topjava.restaurant_voting.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.topjava.restaurant_voting.model.Restaurant;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VoteDto {

    private Long id;
    @JsonIgnore
    private User user;
    private Restaurant restaurant;
    @NotNull
    private LocalDate date;

    public VoteDto(Long id, User user, Restaurant restaurant, LocalDate date) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
    }

    public VoteDto(Vote vote) {
        this.id = vote.getId();
        this.user = vote.getUser();
        this.restaurant = vote.getRestaurant();
        this.date = vote.getDate();
    }

    public VoteDto() {}

}