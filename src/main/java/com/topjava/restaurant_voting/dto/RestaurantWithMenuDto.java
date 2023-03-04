package com.topjava.restaurant_voting.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class RestaurantWithMenuDto {
    private final Long restaurantId;
    private final String restaurantName;
    private List<MealDto> menu;
}
