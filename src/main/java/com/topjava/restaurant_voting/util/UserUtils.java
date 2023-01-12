package com.topjava.restaurant_voting.util;

import com.topjava.restaurant_voting.exeption.IllegalRequestDataException;
import com.topjava.restaurant_voting.model.Role;
import com.topjava.restaurant_voting.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.topjava.restaurant_voting.config.WebSecurityConfig.PASSWORD_ENCODER;

@UtilityClass
public class UserUtils {

    public final static Set<Role> STARTING_ROLES = new HashSet<>();

    static {
        STARTING_ROLES.add(Role.USER);
    }

    public static void assureIdConsistent(User user, int id) {
        if (user.isNew()) {
            user.setId(id);
        } else if (user.id() != id) {
            throw new IllegalRequestDataException(user.getEmail() + " must has id= " + id);
        }
    }

    public static User prepareToSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }

    public Role stringToRole(String string) {
        return switch (string) {
            case ("ROLE_USER") -> Role.USER;
            case ("ROLE_ADMIN") -> Role.ADMIN;
            default -> null;
        };
    }

    public static Set<Role> convertToRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(authority -> stringToRole(authority.getAuthority())).collect(Collectors.toSet());
    }

    public static List<SimpleGrantedAuthority> convertToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).toList();
    }


}
