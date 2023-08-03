package kr.rogarithm.todos.global.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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

    public String getUserIdByToken(String token) {
        return getBodyByToken(token).getSubject();
    }

    public long getExpirationDayByToken(String token) {
        long now = new Date(System.currentTimeMillis()).getTime();
        long expiration = getExpirationByToken(token).getTime();

        return TimeUnit.DAYS.convert((expiration - now), TimeUnit.MILLISECONDS);
    }

    public Date getExpirationByToken(String token) {
        return getBodyByToken(token).getExpiration();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Claims getBodyByToken(String token) throws JwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}