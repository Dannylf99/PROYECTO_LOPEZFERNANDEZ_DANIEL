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
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // Recursos estáticos
                        .requestMatchers("/web/login").permitAll()  // Login accesible sin autenticar
                        .anyRequest().permitAll()  // TODO: Cambiar esto después para proteger rutas
                )
                .formLogin(form -> form.disable())  // Desactivar login de Spring Security
                .csrf(csrf -> csrf.disable())  // Desactivar CSRF temporalmente
                .logout(logout -> logout.disable());  // Desactivar logout de Spring Security

        return http.build();
    }
}