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

    private Long id;
    private Integer user_id;
    private Integer restaurant_id;
    @NotNull
    private LocalDate date;

}