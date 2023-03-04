package com.topjava.restaurant_voting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.topjava.restaurant_voting.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = true)
public class UserDto extends AbstractNamedDto {
    @Email
    @NotBlank
    @Schema(example = "email@gmail.com")
    private final String email;
    @NotBlank
    @Size(min = 3, max = 256)
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDate registered;
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private final Boolean enabled;
}
