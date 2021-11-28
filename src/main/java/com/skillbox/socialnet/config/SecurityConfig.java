package com.skillbox.socialnet.config;

import com.skillbox.socialnet.Constants;
import com.skillbox.socialnet.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final JwtFilter jwtFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/api/v1/auth/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/js/**", "/css/**").permitAll()
                .antMatchers(Constants.API_PLATFORM + "/languages").permitAll()
                .antMatchers(Constants.API_ACCOUNT + "/register", Constants.API_ACCOUNT + "/password/recovery").permitAll()

                .antMatchers("/api/v1/auth/admin/access").hasAnyAuthority("ADMIN") // TODO удалить
                .antMatchers("/api/v1/auth/user/access").hasAnyAuthority("USER")   // TODO удалить

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
