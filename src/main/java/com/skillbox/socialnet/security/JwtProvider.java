package com.skillbox.socialnet.security;

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

    @Value("$(jwt.secret)")
    private String jwtSecret;

    //@Value("$(jwt.expired.milliseconds)")
    private Long expired = 18400000l;

    private final CustomUserDetailsService customUserDetailsService;

    public String generateToken(String userName) {
        Date expiration = new Date(new Date().getTime() + expired);
        Claims claims = Jwts.claims().setSubject(userName);
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
            // TODO обработать в ExceptionsHandler
        } catch (ExpiredJwtException expiredJwtException) {
            log.severe("Token expired");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            log.severe("Unsupported jwt");
        } catch (MalformedJwtException malformedJwtException) {
            log.severe("Malformed jwt");
        } catch (SignatureException signatureException) {
            log.severe("Invalid signature");
        } catch (Exception exception) {
            log.severe("invalid token");
        }
        return false;
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String userName = getUserNameFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
