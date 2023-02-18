package com.topjava.restaurant_voting.mapper;

import com.topjava.restaurant_voting.dto.MealDto;
import com.topjava.restaurant_voting.model.Meal;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = MealMapper.class)
public interface MealMapper {

    MealDto toDTO(Meal meal);

    Meal toModel(MealDto dto);

}
