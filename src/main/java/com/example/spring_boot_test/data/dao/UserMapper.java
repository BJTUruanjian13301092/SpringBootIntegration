package com.example.spring_boot_test.data.dao;

import com.example.spring_boot_test.data.entity.User;
import com.example.spring_boot_test.data.provider.UserProvider;
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

    @InsertProvider(type = UserProvider.class, method="saveAllBySQL")
    void saveAll(@Param("list") Collection<User> users);

    @Update("update user set age = #{age} where name = #{name}")
    void updateSingleUser(User user);

    @Delete("delete from user where name=#{name}")
    void deleteUserByName(String name);

    @SelectProvider(type = UserProvider.class, method="selectByName")
    List<User> selectUserByName(String name);

}
