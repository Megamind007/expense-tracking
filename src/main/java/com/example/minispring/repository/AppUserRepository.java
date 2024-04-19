package com.example.minispring.repository;

import org.apache.ibatis.annotations.*;

import com.example.minispring.model.AppUser;
import com.example.minispring.model.Request.AppUserRequest;
import com.example.minispring.model.Request.AppUserRequestPassword;
import com.example.minispring.model.Response.AppUserResponse;

@Mapper
public interface AppUserRepository {
   @Select("""
           SELECT * FROM users_tb WHERE email = #{email}
        """)
    @Results(id = "UserMap", value = {
           @Result(property = "id", column = "user_id"),
           @Result(property = "email", column = "email"),
           @Result(property = "image", column = "profile_image")
   })
   AppUser findByEmail(String email);

   @Results(id = "UserResponseMap", value = {
        @Result(property = "userId", column = "user_id"),
        @Result(property = "email", column = "email"),
        @Result(property = "image", column = "profile_image")
})
   @Select("""
        select * from users_tb where user_id = #{userId}                
""")
        AppUserResponse findUserById(Integer userId);

        @Select("""
            insert into users_tb(email,password,profile_image) values (#{user.email}, #{user.password}, #{user.image})     
        """)
        void saveUser(@Param("user") AppUserRequest appUserRequest);

        @Select("""
                update users_tb set password = #{appUserRequestPassword} where user_id = #{userId}
        """)
        void changePassword(String appUserRequestPassword,Integer userId);
}
