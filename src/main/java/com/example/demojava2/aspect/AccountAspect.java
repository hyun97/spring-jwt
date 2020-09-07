package com.example.demojava2.aspect;

import com.example.demojava2.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class AccountAspect {

    private final JwtUtil jwtUtil;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Around("@annotation(com.example.demojava2.annotation.JwtService)")
    public Object jwtService(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = jwtUtil.getToken(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());

        if (token == null) {
            //TODO: exception
        }

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody();

        Object res = joinPoint.proceed();

        String renew = jwtUtil.renewJwt(secretKey, expiration, claims);

        return ResponseEntity.ok()
                .header("Set-Cookie", jwtUtil.toCookie(renew))
                .body(res);
    }

    @Around("@annotation(com.example.demojava2.annotation.JwtExpire)")
    public Object jwtExpire(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = jwtUtil.getToken(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());

        if (token == null) {
            //TODO: exception
        }

        Object res = joinPoint.proceed();

        return ResponseEntity.ok().header("Set-Cookie", jwtUtil.toCookie()).body(res);
    }

}
