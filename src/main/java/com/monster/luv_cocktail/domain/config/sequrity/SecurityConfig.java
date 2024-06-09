package com.monster.luv_cocktail.domain.config.security;

import com.monster.luv_cocktail.domain.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("https://localhost:5174"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("*"));
                    config.setAllowCredentials(true);
                    cors.configurationSource(request -> config);
                })
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS,
                                    "/api/authenticate",
                                    "/join/emails/verification-requests",
                                    "/join/emails/verifications",
                                    "/join/submit",
                                    "/weather/api/today",
                                    "/search/api/chart",
                                    "/weather/recommendDefault",
                                    "/api/login").permitAll()
                            .requestMatchers(HttpMethod.POST,
                                    "/customCocktails/**",
                                    "/search/api/chart",
                                    "/api/login",
                                    "/api/recommendations/**").permitAll() // 추가된 부분
                            .requestMatchers(HttpMethod.PUT,
                                    "/customCocktails/**",
                                    "/view/api/cocktails/{id}").permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    "/customCocktails/**",
                                    "/weather/api/today",
                                    "/search/api/cocktails",
                                    "/search/api/chart",
                                    "/weather/recommendDefault",
                                    "/api/recommendations/**").permitAll() // 추가된 부분
                            .requestMatchers(HttpMethod.DELETE,
                                    "/join/withdraw",
                                    "/customCocktails/**").permitAll()
                            .requestMatchers(
                                    "/api/authenticate",
                                    "/join/emails/verification-requests",
                                    "/join/emails/verifications",
                                    "/join/submit",
                                    "/weather/recommendDefault").permitAll()
                            .requestMatchers(HttpMethod.OPTIONS,
                                    "/search/submitTaste",
                                    "/search/updateTasteAndRecommend").hasAuthority("USER")
                            .anyRequest().authenticated();
                })
                .oauth2Login(oauth2Login -> oauth2Login.defaultSuccessUrl("/oauth2/success", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true"));
        return http.build();
    }
}
