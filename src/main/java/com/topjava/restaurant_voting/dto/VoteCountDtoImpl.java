package com.topjava.restaurant_voting.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VoteCountDtoImpl implements VoteCountDto {

    private final Long restaurantId;
    private final String name;
    private final Integer count;

}
