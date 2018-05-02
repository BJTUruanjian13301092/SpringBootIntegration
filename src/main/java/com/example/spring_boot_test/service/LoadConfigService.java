package com.example.spring_boot_test.service;

import com.example.spring_boot_test.data.entity.TestConfigUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoadConfigService {

    @Value("${com.test.name}")
    private String name;

    @Value("${com.test.sex}")
    private String sex;

    @Value("${com.test.age}")
    private String age;

    @Autowired
    TestConfigUser testConfigUser;

    public void loadConfig(){
        System.out.println(name + sex + age);
    }

    public void loadConfig2(){
        System.out.println(testConfigUser.getName()+testConfigUser.getSex()+testConfigUser.getAge());
    }
}
