package com.example.minispring.repository;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import com.example.minispring.model.Opts;

@Mapper
public interface OptsRepository {
    @Results(id = "OptsMapper",value = {
        @Result(property = "optId",column = "opt_id"),
        @Result(property = "optCode",column = "opt_code"),
        @Result(property = "issuedAt",column = "issued_at"),
        @Result(property = "expiredAt",column = "expiration"),
        @Result(property = "verify",column = "verify"),
        @Result(property = "userId",column = "user_id")
    })
    @Select("""
        insert into opts_tb(opt_code,issued_at,expiration,verify,user_id) values(#{optCode},#{issued},#{expiration},#{verify},#{userId})
    """)
    Opts insertOpt(String optCode,LocalDateTime issued,LocalDateTime expiration, Boolean verify, Integer userId);

    @Select("""
        select * from opts_tb where opt_code = #{optCode} order by expiration desc limit 1
    """)
    @ResultMap("OptsMapper")
    Opts confirmOptCode(String optCode);

    @Select("""
        update opts_tb set verify = true where opt_code = #{optCode}
    """)
    @ResultMap("OptsMapper")
    Opts confirmVerifyOptCode(String optCode);

    @Select("""
        select * from opts_tb where user_id = #{userId};
    """)
    @ResultMap("OptsMapper")
    Opts checkUserId(Integer userId);

    @Select("""
        delete from opts_tb where user_id = #{userId};
    """)
    @ResultMap("OptsMapper")
    Opts deleteOptCode(Integer userId);
}
