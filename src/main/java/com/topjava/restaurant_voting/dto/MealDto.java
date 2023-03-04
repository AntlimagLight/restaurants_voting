package com.topjava.restaurant_voting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = true)
public class MealDto extends AbstractNamedDto {
    @Range(min = 0, max = 50000)
    private final int cost;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final LocalDate date;
}
