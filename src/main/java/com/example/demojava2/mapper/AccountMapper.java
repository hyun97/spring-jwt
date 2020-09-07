package com.example.demojava2.mapper;

import com.example.demojava2.model.AccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountMapper {

    AccountVO selectUser(AccountVO account);

}
