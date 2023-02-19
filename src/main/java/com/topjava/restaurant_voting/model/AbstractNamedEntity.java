package com.topjava.restaurant_voting.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;


@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @Column(name = "name", nullable = false)
    protected String name;

    protected AbstractNamedEntity(Long id, String name) {
        super(id);
        this.name = name;
    }
}
