package com.topjava.restaurant_voting.model;

import jakarta.persistence.*;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users", indexes = @Index(columnList = "email", name = "users_unique_email_idx", unique = true))
public class User extends AbstractNamedEntity {

    private String email;
    private String password;
    private LocalDate registered;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            indexes = @Index(columnList = "user_id, role", name = "users_roles_idx", unique = true))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private Boolean enabled;

    private Boolean enableVote;

    public User() {
    }

    public User(Integer id, String name, String email, String password, LocalDate registered, Role... roles) {
        this(id, name, email, password, registered, Arrays.asList((roles)));
    }

    public User(Integer id, String name, String email, String password, LocalDate registered, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.enabled = true;
        this.enableVote = true;
        setRoles(roles);
    }

    public User(Integer id, String name, String email, String password, LocalDate registered) {
        this(id, name, email, password, registered, Collections.emptyList());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDate registered) {
        this.registered = registered;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnableVote() {
        return enableVote;
    }

    public void setEnableVote(Boolean enable_vote) {
        this.enableVote = enable_vote;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registered=" + registered +
                ", roles=" + roles +
                ", enabled=" + enabled +
                ", enable_vote=" + enableVote +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

}
