package com.topjava.restaurant_voting.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RestaurantWithMenuDto {
    private Long restaurantId;
    private String restaurantName;
    private List<MealDto> menu;
}
