package com.topjava.restaurant_voting.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Table(name = "meals", indexes = @Index(columnList = "restaurant_id", name = "meals_restaurant_idx"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "name", "meal_date"})})
public class Meal extends AbstractNamedEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ToString.Exclude
    private Restaurant restaurant;

    @Range(min = 0, max = 50000)
    private int cost;

    @Column(name = "meal_date", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    public Meal(Long id, String name, Integer cost, Restaurant restaurant, LocalDate date) {
        super(id, name);
        this.cost = cost;
        this.restaurant = restaurant;
        this.date = date;
    }

}
