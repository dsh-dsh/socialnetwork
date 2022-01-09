package com.skillbox.socialnet.config;

import com.skillbox.socialnet.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/",
            "/",
            "/static/**",
            "/api/v1/auth/**",
            "/api/v1/platform/**",
            "/api/v1/account/register",
            "/api/v1/account/password/**",
            "/profile/storage/",
            "/storage/",
            "api/v1/admin/login",
            "/favicon.ico",
            "/js/**", "/css/**",
            "/change-password",
            "/login",
            "/shift-email",
            "/swagger-ui/**",
            "/v1/api-docs"
    };

    @Bean
    public FilterRegistrationBean corsFilter() {
        CorsConfigurationSource source = corsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("");
        config.addAllowedHeader("");
        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/", config);
        org.springframework.web.filter.CorsFilter corsFilter = new org.springframework.web.filter.CorsFilter(source);
        FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
        bean.setOrder(0);
        return bean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/").permitAll()
                .antMatchers("/api/v1/auth/login").permitAll()
                .antMatchers("/api/v1/auth/refresh").permitAll()
                .antMatchers("/api/v1/auth/logout").permitAll()
                .antMatchers("/api/v1/account/").permitAll()
                .antMatchers("/api/v1/platform/**").permitAll()
                .antMatchers("/api/v1/geo/**").permitAll()
                .antMatchers("/api/v1/feeds/**").permitAll()
                .antMatchers("/api/v1/users/**").permitAll()
                .antMatchers("/api/v1/post/**").permitAll()
                .antMatchers("/api/v1/tags/**").permitAll()
                .antMatchers("/api/v1/notifications/**").permitAll()
                .antMatchers("/actuator/").permitAll()
                .antMatchers("/api/v1/dialogs/**").permitAll()
                .antMatchers("/api/v1/friends/**").permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .cors(AbstractHttpConfigurer::disable);
    }

    @Value("${cors.urls}")
    private List<String> hosts = new ArrayList<>();

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedOrigins(hosts);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
