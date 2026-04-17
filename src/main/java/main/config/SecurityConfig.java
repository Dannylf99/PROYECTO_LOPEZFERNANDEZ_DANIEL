package main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/web/login").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }
}
