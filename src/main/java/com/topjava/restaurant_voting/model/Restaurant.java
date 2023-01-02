package com.topjava.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    private Integer votes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "restaurant")
    @JsonIgnore
    private List<Meal> menu;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
        this.votes = 0;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public List<Meal> getMenu() {
        return menu;
    }

    public void setMenu(List<Meal> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "votes=" + votes +
                ", menu=" + menu +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
