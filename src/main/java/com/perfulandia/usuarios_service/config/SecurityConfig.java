package com.perfulandia.usuarios_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean // Este método producirá un bean gestionado por Spring
    public PasswordEncoder passwordEncoder() {
        // Retornamos una instancia de BCryptPasswordEncoder,
        // que es una implementación fuerte y recomendada.
        return new BCryptPasswordEncoder();
    }

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF es común para APIs stateless
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite TODAS las peticiones sin autenticación
            );
        return http.build();
    }
}
