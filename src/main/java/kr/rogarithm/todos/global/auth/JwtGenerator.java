package kr.rogarithm.todos.global.auth;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import kr.rogarithm.todos.domain.auth.dto.LoginRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    public String getnerateToken(LoginRequest request) {
        Date now = new Date();

        return Jwts.builder()
                   .setSubject(request.getAccount())
                   .setIssuedAt(now)
                   .signWith(SignatureAlgorithm.HS256, "secret")
                   .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                   .setIssuer("higher-x.com")
                   .setExpiration(new Date(now.getTime() + Duration.ofDays(30).toMillis()))
                   .compact();
    }
}