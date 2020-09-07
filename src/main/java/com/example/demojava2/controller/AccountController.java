package com.example.demojava2.controller;

import com.example.demojava2.annotation.JwtClaim;
import com.example.demojava2.annotation.JwtExpire;
import com.example.demojava2.annotation.JwtService;
import com.example.demojava2.model.AccountVO;
import com.example.demojava2.service.AccountService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/access")
@RestController
public class AccountController {

    private final AccountService accountService;

    @JwtService
    @GetMapping
    public Object userInfo(@JwtClaim Claims claims) {
        HashMap<String, String> userInfo = new HashMap<>();

        userInfo.put("id", claims.get("id", String.class));

        return userInfo;
    }

    @PostMapping
    public ResponseEntity<?> generate(@RequestBody AccountVO account) {
        AccountVO userInfo = accountService.userInfo(account);
        String cookie = accountService.generate(userInfo);

        return ResponseEntity.ok().header("Set-Cookie", cookie).build();
    }

    @JwtService
    @PutMapping
    public ResponseEntity<?> refresh() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @JwtExpire
    @DeleteMapping
    public ResponseEntity<?> delete() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
