package com.topjava.restaurant_voting.mapper;

import com.topjava.restaurant_voting.dto.RestaurantDto;
import com.topjava.restaurant_voting.model.Restaurant;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = RestaurantMapper.class)
public interface RestaurantMapper {

    RestaurantDto toDTO(Restaurant restaurant);

    Restaurant toModel(RestaurantDto dto);

}
