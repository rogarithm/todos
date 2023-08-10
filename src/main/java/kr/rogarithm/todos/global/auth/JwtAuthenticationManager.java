package kr.rogarithm.todos.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationManager {

    private static final String secretKey = "TodosAppSecretLengthIsMoreThan256";

    public JwtAuthenticationManager() {
    }

    public Claims verifyToken(String token) {
        Claims claims = Jwts.parserBuilder()
                          .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                          .build()
                          .parseClaimsJws(token)
                          .getBody();

        if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
            throw new ExpiredJwtException(null, null, "Token has expired");
        }

        return claims;
    }
}