package com.topjava.restaurant_voting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class VoteDto {

    private final long id;
    private final long userId;
    private final long restaurantId;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDate date;

}