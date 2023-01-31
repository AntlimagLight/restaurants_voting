package com.topjava.restaurant_voting.config;

import com.topjava.restaurant_voting.model.Role;
import com.topjava.restaurant_voting.model.User;
import com.topjava.restaurant_voting.repository.UserRepository;
import com.topjava.restaurant_voting.security.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class WebSecurityConfig {
    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Authenticating {}", email);
            User user = userRepository.findByEmailIgnoreCase(email).
                    orElseThrow(() -> new UsernameNotFoundException("User with this email not found: " + email));
            return AuthUser.build(user);
        };
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .requestMatchers("/user/**", "/admin/**").authenticated()
                .requestMatchers("/h2/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/registration").anonymous()
                .anyRequest().anonymous()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }
}
