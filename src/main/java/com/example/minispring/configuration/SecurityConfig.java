package com.example.minispring.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

import com.example.minispring.jwt.JwtAuthEntrypoint;
import com.example.minispring.jwt.JwtAuthFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
   private final JwtAuthFilter jwtAuthFilter;
   private final JwtAuthEntrypoint jwtAuthEntrypoint;

   @Bean
   AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
       return configuration.getAuthenticationManager();
   }

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
       http
               .cors(withDefaults()).csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(request -> request
                       .requestMatchers("/auth/**","/v3/api-docs/**",
                               "/swagger-ui/**",
                               "/swagger-ui.html","/mail","/sendmail","/api/v1/mail","emailing-controller/sendMail","/api/v1/files","/api/v1/files/**"
                       ).permitAll()
                       .anyRequest().authenticated())
               .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntrypoint))
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
       return http.build();
   }}
