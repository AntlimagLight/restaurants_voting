package com.topjava.restaurant_voting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    private long id;
    private long userId;
    private long restaurantId;
    @NotNull
    private LocalDate date;

}