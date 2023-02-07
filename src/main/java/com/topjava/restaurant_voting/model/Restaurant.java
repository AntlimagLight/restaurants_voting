package com.topjava.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Restaurant extends AbstractNamedEntity {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "restaurant")
    @JsonIgnore
    @ToString.Exclude
    private List<Vote> votes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "restaurant")
    @JsonIgnore
    @ToString.Exclude
    private List<Meal> menu;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

}

