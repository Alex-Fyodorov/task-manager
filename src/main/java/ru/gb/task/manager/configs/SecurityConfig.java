package ru.gb.task.manager.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.gb.task.manager.services.UserService;

@Configuration
@Slf4j
@RequiredArgsConstructor
//@PropertySource("secrets.properties")
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("===============================================================");
        log.info("Dao Authentication Provider");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorise -> authorise
                        .requestMatchers(HttpMethod.POST, "/api/v1/tasks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tasks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tasks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/priority").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/executor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/admin_view").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/my").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/id/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/tasks/status").authenticated()
                        .requestMatchers("/api/v1/comments").authenticated()
                        .requestMatchers("/api/v1/priorities").authenticated()
                        .requestMatchers("/api/v1/statuses").authenticated()
                        .requestMatchers("/api/v1/authenticate").permitAll()
                        .anyRequest().permitAll())
                .exceptionHandling(eh -> eh.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
