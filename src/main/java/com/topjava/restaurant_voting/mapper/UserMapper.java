package com.topjava.restaurant_voting.mapper;

import com.topjava.restaurant_voting.dto.UserDto;
import com.topjava.restaurant_voting.model.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserMapper {

    UserDto toDTO(User user);

    User toModel(UserDto dto);

}
