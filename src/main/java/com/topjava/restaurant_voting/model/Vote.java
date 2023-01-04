package com.topjava.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private Integer restaurant_id;

    public Vote(Integer id, User user, Integer restaurant_id) {
        this.id = id;
        this.user = user;
        this.restaurant_id = restaurant_id;
    }

    public Vote() {

    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }
}
