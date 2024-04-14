package com.example.eumserver.domain.jwt;

import com.example.eumserver.domain.user.Name;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final Key jwtSecret;

    @Value("${JWT_ACCESS_EXPIRATION_TIME}")
    private long AC_EXPIRATION_IN_MS;

    @Value("${JWT_REFRESH_EXPIRATION_TIME}")
    private long RF_EXPIRATION_IN_MS;

    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_AVATAR = "avatar";
    private static final String CLAIM_AUTHORITIES = "authorities";
    private static final String DELIMITER = ",";

    public JwtTokenProvider(
            @Value("${jwt.secret}") String jwtSecretStr
    ) {
        this.jwtSecret = Keys.hmacShaKeyFor(jwtSecretStr.getBytes());
    }

    public String generateAccessToken(PrincipalDetails principalDetails) {
        String authorities = principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER));
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(String.valueOf(principalDetails.getUserId()))
                .claim(CLAIM_EMAIL, principalDetails.getEmail())
                .claim(CLAIM_NAME, principalDetails.getName())
                .claim(CLAIM_AVATAR, principalDetails.getAvatar())
                .claim(CLAIM_AUTHORITIES, authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + AC_EXPIRATION_IN_MS))
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(PrincipalDetails principalDetails) {
        String authorities = principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER));
        long now = System.currentTimeMillis();

        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(principalDetails.getUserId()))
                .claim(CLAIM_EMAIL, principalDetails.getEmail())
                .claim(CLAIM_NAME, principalDetails.getName())
                .claim(CLAIM_AVATAR, principalDetails.getAvatar())
                .claim(CLAIM_AUTHORITIES, authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + RF_EXPIRATION_IN_MS))
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();

        redisTemplate.opsForValue().set(
                String.valueOf(principalDetails.getUserId()),
                refreshToken,
                RF_EXPIRATION_IN_MS,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            throw JwtTokenInvalidException.INSTANCE;
        }
    }

    /**
     * DB 조회가 이뤄진다면 JWT 토큰의 장점을 얻어가지 못한다.
     *
     * @link https://velog.io/@tlatldms/%EC%84%9C%EB%B2%84%EA%B0%9C%EB%B0%9C%EC%BA%A0%ED%94%84-Spring-security-refreshing-JWT-DB%EC%A0%91%EA%B7%BC%EC%97%86%EC%9D%B4-%EC%9D%B8%EC%A6%9D%EA%B3%BC-%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        long userId = Long.parseLong(claims.getSubject());
        String email = claims.get(CLAIM_EMAIL, String.class);
        Name name = new Name(claims.get(CLAIM_NAME, String.class), "");
        String avatar = claims.get(CLAIM_AVATAR, String.class);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(CLAIM_AUTHORITIES).toString().split(DELIMITER))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        PrincipalDetails principalDetails = new PrincipalDetails(userId, email, name, avatar, authorities);
        return new UsernamePasswordAuthenticationToken(principalDetails, null, authorities);
    }
}
