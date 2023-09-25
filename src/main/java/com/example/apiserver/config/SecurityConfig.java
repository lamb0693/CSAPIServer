package com.example.apiserver.config;


import com.example.apiserver.filter.JwtTokenCheckFilter;
import com.example.apiserver.filter.LoginFilter;
import com.example.apiserver.handler.CustomLoginSuccessHandler;
import com.example.apiserver.service.MemberUserDetailsService;
import com.example.apiserver.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Log4j2
public class SecurityConfig {
    MemberUserDetailsService memberUserDetailsService;
    JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //*****AuthManager build 및 설정
        // Login Filter를 위해서 만든 듯
        // 원래는 http.setUserDetail만 있으면 될 듯 한데
        // 이렇게 하면 모든 과정에 이 Authentication Manager가 불릴 듯함
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(memberUserDetailsService);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);

        // Login filter 설정
        LoginFilter loginFilter = new LoginFilter("/auth/login");
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());

        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
        //***** Custom Login Filter 설정 끝

        // JwtTokenCheck filter 설정
        JwtTokenCheckFilter jwtTokenCheckFilter = new JwtTokenCheckFilter(jwtUtil, memberUserDetailsService);
        http.addFilterBefore(jwtTokenCheckFilter, UsernamePasswordAuthenticationFilter.class);
        // JwtTokenCheck filter 설정 끝

        http.authorizeHttpRequests( (request) -> {
            request.requestMatchers("/", "/register").permitAll()
                    .requestMatchers("/js/**", "/image/**", "/css/**").permitAll()
                    .requestMatchers("/member/register").permitAll()
                    .requestMatchers("/api/member/**").hasAuthority("ROLE_CSR")
                    .anyRequest().authenticated();
        });
        http.sessionManagement( (session) -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)   ;
        });
        http.csrf(AbstractHttpConfigurer::disable);
//
//        http.cors(httpSecurityCorsConfigurer -> {
//            httpSecurityCorsConfigurer.configurationSource((corsConfigurationSource()));
//        });


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("/**", "/files/**"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CustomLoginSuccessHandler customLoginSuccessHandler(){
        return new CustomLoginSuccessHandler(jwtUtil);
    }
}
