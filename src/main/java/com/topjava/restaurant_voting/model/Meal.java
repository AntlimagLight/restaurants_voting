package com.topjava.restaurant_voting.model;

import jakarta.persistence.*;

@Entity
@Table(name = "meals",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id"}, name = "meals_unique_restaurant_idx")})
public class Meal extends AbstractNamedEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    private Integer cost;

    public Meal() {
    }

    public Meal(Integer id, String name, Integer cost) {
        super(id, name);
        this.cost = cost;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
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
