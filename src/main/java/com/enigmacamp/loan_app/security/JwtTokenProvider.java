package com.enigmacamp.loan_app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    @Value("${loan-app.secretKey}")
    private String SECRET_KEY;
    @Value("${loan-app.expiration}")
    private Long EXPIRATION_TIME;

    // mengekstrak username dari token JWT
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getSubject();
    }

    // validasi token JWT
    public boolean isValidToken(String token) {
        try {
            DecodedJWT decodedJWT = getDecodedJWT(token);

            return !decodedJWT.getExpiresAt().before(new Date());
//              return decodedJWT.getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getRoleFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        String role = decodedJWT.getClaim("role").asString();

        return role;
    }

    public String generateToken(String username, List<String> role) {
        String token = JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withExpiresAt(new Date((System.currentTimeMillis() + EXPIRATION_TIME)))
                .sign(Algorithm.HMAC512(SECRET_KEY));

        return token;
    }

    public Long getExpirationTime(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        Date expirationDate = decodedJWT.getExpiresAt();
        return expirationDate.getTime() - System.currentTimeMillis();
    }

    private DecodedJWT getDecodedJWT(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token);// verifikasi dan decode token
        return decodedJWT;
    }
}
