package com.topjava.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Table(name = "users", indexes = @Index(columnList = "email", name = "users_unique_email_idx", unique = true))
public class User extends AbstractNamedEntity {

    @Size(min = 3, max = 128)
    @Email
    @NotBlank
    @Schema(example = "email@gmail.com")
    private String email;

    @NotBlank
    @Size(min = 3, max = 256)
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate registered;
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            indexes = @Index(columnList = "user_id, role", name = "users_roles_idx", unique = true))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private Boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @OrderBy("date DESC")
    @JsonIgnore
    @ToString.Exclude
    private List<Vote> votes;

    public User(Long id, String name, String email, String password, LocalDate registered, Boolean enabled, Role... roles) {
        this(id, name, email, password, registered, enabled, Arrays.asList((roles)));
    }

    public User(Long id, String name, String email, String password, LocalDate registered, Boolean enabled, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.enabled = true;
        setRoles(roles);
    }

    public User(Long id, String name, String email, String password, LocalDate registered, Boolean enabled) {
        this(id, name, email, password, registered, enabled, Collections.emptyList());
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

}
