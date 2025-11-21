package co.edu.unimagdalena.busreserve.security.config;

import co.edu.unimagdalena.busreserve.security.jwt.JwtAuthenticationFilter;
import co.edu.unimagdalena.busreserve.security.error.Http401EntryPoint;
import co.edu.unimagdalena.busreserve.security.error.Http403AccessDenied;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final Http401EntryPoint authEntryPoint;
    private final Http403AccessDenied accessDenied;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDenied)
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/routes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/trips/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/trips/*/seats/*/hold")
                        .hasAnyAuthority("PASSENGER", "CLERK")

                        .requestMatchers(HttpMethod.POST, "/api/trips/*/tickets")
                        .hasAnyAuthority("PASSENGER", "CLERK")

                        .requestMatchers(HttpMethod.POST, "/api/tickets/*/cancel")
                        .hasAuthority("PASSENGER")

                        .requestMatchers(HttpMethod.POST, "/api/trips/*/assign")
                        .hasAuthority("DISPATCHER")

                        .requestMatchers(HttpMethod.POST, "/api/trips/*/boarding/open")
                        .hasAuthority("DISPATCHER")

                        .requestMatchers(HttpMethod.POST, "/api/trips/*/boarding/close")
                        .hasAuthority("DISPATCHER")

                        .requestMatchers(HttpMethod.POST, "/api/trips/*/depart")
                        .hasAuthority("DISPATCHER")

                        .requestMatchers(HttpMethod.POST, "/api/parcels")
                        .hasAnyAuthority("CLERK", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/parcels/*/status")
                        .hasAnyAuthority("DRIVER", "CLERK")

                        .requestMatchers(HttpMethod.POST, "/api/payments/confirm")
                        .hasAnyAuthority("CLERK", "DRIVER")

                        .requestMatchers(HttpMethod.POST, "/api/cash/close")
                        .hasAnyAuthority("CLERK", "DRIVER")

                        .requestMatchers(HttpMethod.PUT, "/api/admin/config")
                        .hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/admin/metrics")
                        .hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}