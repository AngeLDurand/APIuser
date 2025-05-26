package com.perfulandia.usuarios_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
  @Bean // Este método producirá un bean gestionado por Spring
    public PasswordEncoder passwordEncoder() {
        // Retornamos una instancia de BCryptPasswordEncoder,
        // que es una implementación fuerte y recomendada.
        return new BCryptPasswordEncoder();
    }
}
