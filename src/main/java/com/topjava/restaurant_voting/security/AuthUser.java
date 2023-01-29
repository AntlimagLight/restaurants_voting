package com.topjava.restaurant_voting.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.topjava.restaurant_voting.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.topjava.restaurant_voting.util.UserUtils.convertToAuthorities;

@SuppressWarnings("FieldMayBeFinal")
public class AuthUser implements UserDetails {

    private Integer id;
    private String userName;
    private String email;
    @JsonIgnore
    private String password;
    private Boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Integer id, String userName, String email, String password, Boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static AuthUser build(User user) {
        return new AuthUser(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getEnabled(),
                convertToAuthorities(user.getRoles()));
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
