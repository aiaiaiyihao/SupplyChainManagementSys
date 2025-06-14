package org.yihao.authserver.Secutiry;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.yihao.authserver.Secutiry.Service.UserDetailsImpl;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

/*    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;*/

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove Bearer prefix
        }
        return null;
    }

    public String generateTokenFromUsername(String username) {
        System.out.println("JWT Expiration Time (ms): " + jwtExpirationMs);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String generateTokenFromUser(UserDetailsImpl user) {
        return Jwts.builder()
                .subject(user.getUsername()) // typically the email or username
                .claim("email", user.getEmail())
                .claim("role", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())) // "SUPPLIER", "DRIVER", etc.
                .claim("userId", user.getId())
                .claim("tableId", user.getTableId())
                .claim("userName", user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

 /*   public String getJwtFromCookies(HttpServletRequest request) {
        *//*request: The incoming HttpServletRequest object.
        jwtCookie: The name of the cookie you want to retrieve.
        Returns: The Cookie object if found, otherwise null.*//*
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            //the value is JWT token
            return cookie.getValue();
        } else {
            return null;
        }
    }

    //UserDetails already customized
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        // Generate JWT token using the user's username
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());

        // Create a response cookie with the JWT token
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/api") // The cookie will only be sent for requests to the "/api" path
                .maxAge(24 * 60 * 60) // Set cookie expiration to 24 hours (in seconds)
                .httpOnly(false) // false means the cookie can be accessed via JavaScript (less secure)
                .secure(false) // false means the cookie can be sent over HTTP (should be true for HTTPS)
                .build(); // Build the cookie object

        return cookie; // Return the created cookie
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
                .path("/api")
                .build();
        return cookie;
    }*/


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}

