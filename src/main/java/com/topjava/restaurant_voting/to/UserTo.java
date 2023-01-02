package com.topjava.restaurant_voting.to;

import com.topjava.restaurant_voting.model.Role;
import com.topjava.restaurant_voting.model.User;

import java.time.LocalDate;
import java.util.Set;

public class UserTo {

    private Integer id;
    private String name;
    private String email;
    private LocalDate registered;
    private Set<Role> roles;
    private Boolean enabled;
    private Boolean enable_vote;

    public UserTo() {
    }

    public UserTo(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.registered = user.getRegistered();
        this.enabled = user.getEnabled();
        this.enable_vote = user.getEnableVote();
        this.roles = user.getRoles();
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "email='" + email + '\'' +
                ", registered=" + registered +
                ", roles=" + roles +
                ", enabled=" + enabled +
                ", enable_vote=" + enable_vote +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
}
