package com.topjava.restaurant_voting.repository;

import com.topjava.restaurant_voting.dto.VoteCountDto;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserAndDate(User user, LocalDate date);

    List<Vote> findAllByUser(User user);

    @Query(value = "SELECT v.restaurant.id as restaurantId, COUNT(v.restaurant.id) as count, r.name as name" +
            " FROM Vote v LEFT JOIN Restaurant r ON v.restaurant.id=r.id WHERE v.date= :date GROUP BY v.restaurant.id")
    List<VoteCountDto> findAllByDate(@Param("date") LocalDate date);

}
