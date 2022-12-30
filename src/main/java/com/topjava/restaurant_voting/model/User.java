package com.topjava.restaurant_voting.model;

import jakarta.persistence.*;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}, name = "users_unique_email_idx")})
public class User extends AbstractNamedEntity {

    private String email;
    private String password;
    private Date registered;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_idx")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private Boolean enabled;

    private Boolean enable_vote;

    public User() {
    }

    public User(User u) {
        this(u.id, u.name, u.email, u.password, u.registered, u.roles);
        this.enabled = u.enabled;
        this.enable_vote = u.enable_vote;
    }

    public User(Integer id, String name, String email, String password, Date registered, Role... roles) {
        this(id, name, email, password, registered, Arrays.asList((roles)));
    }

    public User(Integer id, String name, String email, String password, Date registered, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.enabled = true;
        this.enable_vote = true;
        setRoles(roles);
    }

    public User(Integer id, String name, String email, String password, Date registered) {
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

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
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

    public Boolean getEnable_vote() {
        return enable_vote;
    }

    public void setEnable_vote(Boolean enable_vote) {
        this.enable_vote = enable_vote;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registered=" + registered +
                ", roles=" + roles +
                ", enabled=" + enabled +
                ", enable_vote=" + enable_vote +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

}
