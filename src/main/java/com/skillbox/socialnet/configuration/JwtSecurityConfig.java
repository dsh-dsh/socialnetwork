package com.skillbox.socialnet.configuration;

import com.skillbox.socialnet.security.JwtFilter;
import com.skillbox.socialnet.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
public class JwtSecurityConfig { //extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

//    @Autowired
//    private JwtFilter jwtFilter;
//
//    public void configure(HttpSecurity http) {
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//    }

}
