package tn.talan.tripaura_backend.configurations;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.servlet.HandlerExceptionResolver;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;
import tn.talan.tripaura_backend.security.JwtAuthenticationFilter;
import tn.talan.tripaura_backend.security.JwtService;

@Configuration
@EnableWebSecurity
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "tn.talan.tripaura_backend.repositories")
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserTripAuraRepo userRepository;
    public HandlerExceptionResolver handlerExceptionResolver;
    public JwtService jwtService;
    public UserDetailsService userDetailsService;

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter(HandlerExceptionResolver handlerExceptionResolver,
            JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(handlerExceptionResolver, jwtService, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())

                // http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/", "/index", "/loginFailure", "/TripAuraUsers/login", "/auth/loginSuccess",
                                "http://localhost:8081/swagger-ui/index.html", "http://localhost:4200/homeN",
                                "/TripAuraUsers/Add/TRAVELER", "/auth/forgotPassword",
                                "/Get/TripAuraActivity", "/get/TripAuraActivity/{id}", "/Get/TripAuraActivity",
                                "/update/ActivityTripAura/{id}", "/delete/ActivityTripAura/{id}")
                        .permitAll()
                       // .requestMatchers("/flights/**").hasAnyRole("FLIGHT_AGENT", "SUPER_ADMIN")
                        .requestMatchers("/airlines/**").hasAnyRole("FLIGHT_AGENT", "SUPER_ADMIN")

                        .requestMatchers("/accommodations/**").hasAnyRole("HOTEL_AGENT",
                        "SUPER_ADMIN")
                        .requestMatchers("/activities/**").hasAnyRole("ACTIVITY_AGENT", "SUPER_ADMIN")
                        .requestMatchers("/dashboard/users/**").hasAnyRole("ACCOUNT_AGENT", "SUPER_ADMIN")
                        .requestMatchers("/websocket/**").permitAll()
                        .requestMatchers("/TripAuraUsers/Add/**").permitAll()
                        .requestMatchers("/qrcode/**").permitAll()
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .defaultSuccessUrl("/auth/loginSuccess", true)
                        .failureUrl("/auth/loginFailure"))
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll());

        http.addFilterBefore(authenticationJwtTokenFilter(handlerExceptionResolver, jwtService, userDetailsService),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return email -> (UserDetails) userRepository.findUserTripAuraByEmail(email);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

}