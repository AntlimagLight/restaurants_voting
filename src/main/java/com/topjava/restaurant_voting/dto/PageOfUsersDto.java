package com.topjava.restaurant_voting.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class PageOfUsersDto {

    private final int totalPages;
    private final long totalElements;
    private final int size;
    private final int pageNumber;
    private final List<UserDto> users;

}
