package com.topjava.restaurant_voting.dto;

import java.time.LocalDate;

public interface RestaurantOwnedMealDto {

    Long getId();

    Long getRestaurantId();

    String getRestaurantName();

    String getName();

    Integer getCost();

    LocalDate getDate();

}
