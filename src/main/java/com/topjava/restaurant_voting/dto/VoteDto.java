package com.topjava.restaurant_voting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class VoteDto {

    private long id;
    private long userId;
    private long restaurantId;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;

}