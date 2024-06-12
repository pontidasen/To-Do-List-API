package com.todolist.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    //ใช้สำหรับ Config ข้อมูลของ User
    public SecurityConfig(UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    //การเข้ารหัส Password
    @Bean
    public  static PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    //ตัวจัดการ Authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }



    //การกำหนด Path เราท่ี่ใช้ในการป้องกัน
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //ใช้ http ปิดการยืนยันตัวตน ถ้ามีการเข้ามาที่ path /api/auth/**
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html,/v2/api-docs/**,/swagger-resources/**").permitAll() // เพิ่มการตั้งค่าสำหรับ Swagger
                                .anyRequest().authenticated()

                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }
}

