package com.example.demojava2.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    public String getToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(e -> e.getName().equals("Token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public String newJwt(String secretKey, Long expiration, Map<String, Object> claims) {
        Date createdDate = new Date();
        Date expirationDate =
                new Date(createdDate.getTime() +
                        TimeUnit.MILLISECONDS.convert(expiration, TimeUnit.SECONDS));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String renewJwt(String secretKey, Long expiration, Claims claims) {
        Date date = new Date();

        claims.setExpiration(
                new Date(date.getTime() + TimeUnit.MILLISECONDS.convert(expiration, TimeUnit.SECONDS)));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String toCookie(String value) {
        return ResponseCookie
                .from("Token", value)
                .httpOnly(true) // JS 에서 쿠키에 접근 불가
                .sameSite("None")
                .build()
                .toString();
    }

    public String toCookie() {
        return ResponseCookie
                .from("Token", "")
                .maxAge(0)
                .build()
                .toString();
    }

}
