package com.topjava.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "meals", indexes = @Index(columnList = "restaurant_id", name = "meals_restaurant_idx"))
public class Meal extends AbstractNamedEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    @NotNull
    @Range(min = 0, max = 50000)
    private Integer cost;

    @Column(name = "meal_date", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date;

    public Meal(Integer id, String name, Integer cost, Restaurant restaurant, LocalDate date) {
        super(id, name);
        this.cost = cost;
        this.restaurant = restaurant;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "cost=" + cost +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
