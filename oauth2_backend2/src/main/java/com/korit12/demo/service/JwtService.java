package com.korit12.demo.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    static final long EXPIRATIONTIME = 86400000;
    static final String PREFIX = "Bearer";
    static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis()+EXPIRATIONTIME))
                .signWith(KEY)
                .compact();
    }

    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith(PREFIX)) {
            String authToken = token.substring(PREFIX.length()).trim();
            String user = Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(authToken)
                    .getPayload()
                    .getSubject();
            if (user != null) {
                return user;
            }
        }
        return null;
    }
}
