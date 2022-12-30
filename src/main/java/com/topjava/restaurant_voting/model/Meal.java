package com.topjava.restaurant_voting.model;

import jakarta.persistence.*;

@Entity
@Table(name = "meals", indexes = @Index(columnList = "restaurant_id", name = "meals_restaurant_idx"))
public class Meal extends AbstractNamedEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    private Integer cost;

    public Meal() {
    }

    public Meal(Integer id, String name, Integer cost, Restaurant restaurant) {
        super(id, name);
        this.cost = cost;
        this.restaurant = restaurant;
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
