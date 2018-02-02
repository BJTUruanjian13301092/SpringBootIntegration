package com.example.spring_boot_test.dao;

import com.example.spring_boot_test.entity.User;
import com.example.spring_boot_test.service.UserInsertProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    @Select("select * from user")
    List<User> findAllUser();

    @Insert("insert into user(name,sex,age) values (#{name},#{sex},#{age})")
    void addUser(User user);

    @InsertProvider(type= UserInsertProvider.class, method="saveAll")
    void saveAll(@Param("list") Collection<User> users);

    @Update("update user set age = #{age} where name = #{name}")
    void updateSingleUser(User user);

    @Delete("delete from user where name=#{name}")
    void deleteUserByName(String name);

}
