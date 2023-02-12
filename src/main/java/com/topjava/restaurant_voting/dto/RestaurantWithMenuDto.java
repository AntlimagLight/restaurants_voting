package com.topjava.restaurant_voting.dto;

import com.topjava.restaurant_voting.model.Meal;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RestaurantWithMenuDto {
    Long restaurantId;
    String restaurantName;
    private List<Meal> menu;
}
