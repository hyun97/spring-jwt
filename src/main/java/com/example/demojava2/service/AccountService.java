package com.example.demojava2.service;

import com.example.demojava2.mapper.AccountMapper;
import com.example.demojava2.model.AccountVO;
import com.example.demojava2.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    private final AccountMapper accountMapper;
    private final JwtUtil jwtUtil;

    public AccountVO userInfo(AccountVO account) {
        if (account.getId().isEmpty() || account.getPassword().isEmpty()) {
            //TODO: exception
        }

        AccountVO accountVO = accountMapper.selectUser(account);

        if (accountVO == null) {
            //TODO: exception
        }

        return accountVO;
    }

    public String generate(AccountVO account) {
        if (account.getId().isEmpty()) {
            //TODO: exception
        }

        String uuid = UUID.randomUUID().toString();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("uuid", uuid);
        claims.put("id", account.getId());

        String jwt = jwtUtil.newJwt(secretKey, expiration, claims);

        return jwtUtil.toCookie(jwt);
    }

}
