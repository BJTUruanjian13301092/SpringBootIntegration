package com.example.spring_boot_test.controller;

import com.example.spring_boot_test.dao.TestInterface;
import com.example.spring_boot_test.dao.UserMapper;
import com.example.spring_boot_test.entity.User;
import com.example.spring_boot_test.service.LoadConfigService;
import com.example.spring_boot_test.service.RedisTestService;
import com.example.spring_boot_test.service.StandfordNLPService;
import com.example.spring_boot_test.service.TestLibSVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TestLibSVM testLibSVM;

    @Autowired
    RedisTestService redisTestService;

    @Autowired
    TestInterface testInterface;

    @Autowired
    StandfordNLPService standfordNLPService;

    @Autowired
    LoadConfigService loadConfigService;

    /**
     * 使用Mybatis增删改查
     */

    //保存单个用户
    @RequestMapping("saveuser")
    public void saveUser() {
        User user = new User();
        user.setName("Larry");
        user.setAge("22");
        user.setSex("Male");
        userMapper.addUser(user);
    }

    //查找所有用户
    @RequestMapping("finduser")
    public List<User> findUser(){
        return userMapper.findAllUser();
    }

    //批量插入用户
    @RequestMapping("saveusers")
    public void saveUsers(){
        User user_1 = new User();
        User user_2 = new User();

        user_1.setName("Jack");
        user_1.setSex("Male");
        user_1.setAge("18");

        user_2.setName("Lucy");
        user_2.setSex("Female");
        user_2.setAge("18");

        List<User> users = new ArrayList<>();
        users.add(user_1);
        users.add(user_2);
        userMapper.saveAll(users);
    }

    //修改单个用户
    @RequestMapping("updateuser")
    public void updateUser(){
        User user = new User();
        user.setName("Larry");
        user.setAge("23");
        user.setSex("Male");
        userMapper.updateSingleUser(user);
    }

    //删除单个用户(By Name)
    @RequestMapping("deleteuser")
    public void deleteUser(){
        userMapper.deleteUserByName("Lucy");
    }

    //测试SVM
    @RequestMapping("testSVM")
    public void testSvm() throws IOException{
        testLibSVM.testSVM();
    }

    //测试Redis
    @RequestMapping("teststringredis")
    public void testStringRedisTemplate(){

        redisTestService.testStringRedisTemplate();
    }

    //测试Interface
    @RequestMapping("testInterface")
    public void testInterface(){

        testInterface.testMyInterface();
    }

    //测试StandFordNlp
    @RequestMapping("testnlp")
    public void testNlp(){

        standfordNLPService.testMyNlp();
    }

    @RequestMapping("testconfig")
    public void testConfig(){
        loadConfigService.loadConfig();
    }

    @RequestMapping("testconfig2")
    public void testConfig2(){
        loadConfigService.loadConfig2();
    }
}
