package com.topjava.restaurant_voting.mapper;

import com.topjava.restaurant_voting.dto.VoteDto;
import com.topjava.restaurant_voting.model.Vote;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = VoteMapper.class)
public interface VoteMapper {

    default VoteDto toDTO(Vote vote) {
        if (vote == null) {
            return null;
        }
        return new VoteDto(vote.getId(),
                vote.getUser().getId(),
                vote.getRestaurant().getId(),
                vote.getDate());
    }

}
