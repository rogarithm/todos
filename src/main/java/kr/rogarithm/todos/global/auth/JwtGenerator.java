package kr.rogarithm.todos.global.auth;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Date;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    public String generateAccessToken(LoginRequest request) {
        return generateTokenWithDay(request.getAccount(), 1);
    }

    public String generateRefreshToken(LoginRequest request) {
        return generateTokenWithDay(request.getAccount(), 30);
    }

    private String generateTokenWithDay(String account, int days) {

        Date now = new Date();

        return Jwts.builder()
                   .setSubject(account)
                   .setIssuedAt(now)
                   .signWith(Keys.hmacShaKeyFor("TodosAppSecretLengthIsMoreThan256".getBytes()),
                           SignatureAlgorithm.HS256)
                   .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                   .setIssuer("higher-x.com")
                   .setExpiration(new Date(now.getTime() + Duration.ofDays(days).toMillis()))
                   .compact();
    }
}