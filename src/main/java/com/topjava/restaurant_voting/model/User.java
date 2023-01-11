package com.topjava.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

import static com.topjava.restaurant_voting.config.WebSecurityConfig.PASSWORD_ENCODER;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "users", indexes = @Index(columnList = "email", name = "users_unique_email_idx", unique = true))
public class User extends AbstractNamedEntity {

    @Size(min = 3, max = 128)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 3, max = 256)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate registered;
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            indexes = @Index(columnList = "user_id, role", name = "users_roles_idx", unique = true))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private Boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @OrderBy("date DESC")
    @JsonIgnore
    private List<Vote> votes;

    public User(Integer id, String name, String email, String password, LocalDate registered, Boolean enabled, Role... roles) {
        this(id, name, email, password, registered, enabled, Arrays.asList((roles)));
    }

    public User(Integer id, String name, String email, String password, LocalDate registered, Boolean enabled, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.enabled = true;
        setRoles(roles);
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public User(Integer id, String name, String email, String password, LocalDate registered, Boolean enabled) {
        this(id, name, email, password, registered, enabled, Collections.emptyList());
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registered=" + registered +
                ", roles=" + roles +
                ", enabled=" + enabled +
                ", votes=" + votes +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
