package com.foroalura.security;

import com.foroalura.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Deshabilitar CSRF porque usas JWT y API REST
                .csrf(csrf -> csrf.disable())

                // Configurar rutas públicas y protegidas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Login y registro sin autenticación
                        .requestMatchers(HttpMethod.GET, "/topicos", "/topicos/**").permitAll() // Listar y obtener tópicos sin autenticación
                        .requestMatchers(HttpMethod.POST, "/topicos").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/topicos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/topicos/**").authenticated()
                        .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
                )

                // No crear sesión, usar JWT stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Usar el provider que sabe de usuarios y encriptación
                .authenticationProvider(authenticationProvider())

                // Añadir filtro JWT antes que el filtro estándar de usuario/password
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // Encoder para passwords (BCrypt recomendado)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Provider que usa el CustomUserDetailsService y el encoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Exponer el AuthenticationManager para inyectarlo en otros lugares (p. ej. controladores)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
