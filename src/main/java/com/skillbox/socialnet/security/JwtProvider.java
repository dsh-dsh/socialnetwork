package com.skillbox.socialnet.security;

import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.util.Constants;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
@Log
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expired.milliseconds}")
    private int expired;

    private final CustomUserDetailsService customUserDetailsService;

    public String generateToken(Person person) {
        Date expiration = new Date(new Date().getTime() + expired);
        Claims claims = Jwts.claims().setSubject(person.getEMail());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            log.warning(Constants.TOKEN_EXPIRED_MESSAGE);
            return false;
        } catch (RuntimeException exception) {
            // UnsupportedJwtException MalformedJwtException SignatureException
            log.warning(Constants.INVALID_TOKEN_MESSAGE);
            return false;
        }
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String requestToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return hasText(requestToken) ? requestToken : null;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String userName = getUserNameFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
