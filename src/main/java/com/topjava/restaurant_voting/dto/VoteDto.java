package com.topjava.restaurant_voting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;

}