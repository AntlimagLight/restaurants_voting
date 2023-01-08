package com.topjava.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

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

    public Meal(Integer id, String name, Integer cost, Restaurant restaurant) {
        super(id, name);
        this.cost = cost;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", id=" + id +
                '}';
    }
}
