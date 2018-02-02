package com.example.spring_boot_test.service;

import com.example.spring_boot_test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTestService {

    @Autowired
    public StringRedisTemplate redisTemplate;

    public void testStringRedisTemplate(){

        //设置缓存，建议每个键都设置过期时间
        //redisTemplate.opsForValue().set("test", "hello I am value", 10, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("test", "hello I am value");
        //获取缓存值
        String value = redisTemplate.opsForValue().get("test");
        System.out.println(value);

        //删除某个键
        redisTemplate.delete("test");

        //操作set
        redisTemplate.opsForSet().add("testSet", "123");
        redisTemplate.opsForSet().add("testSet", "456");
        redisTemplate.opsForSet().add("testSet", "789");
        Set<String> members = redisTemplate.opsForSet().members("testSet");//获取set内的所有值
        System.out.println(members.toString());

        redisTemplate.opsForSet().remove("testSet", "123","456");//移除set内的多个对象
        members = redisTemplate.opsForSet().members("testSet");
        System.out.println(members.toString());

        //操作list
        redisTemplate.opsForList().rightPush("testList", "1");
        redisTemplate.opsForList().rightPush("testList", "2");
        //获取list内的所有元素
        List<String> list = redisTemplate.opsForList().range("testList", 0, -1);
        System.out.println(list.toString());

        //操作zset
        redisTemplate.opsForZSet().add("testZSet","Larry",1);
        redisTemplate.opsForZSet().add("testZSet","Euphie",2);
        redisTemplate.opsForZSet().add("testZSet","Jack",3);
        Set<String> zmembers = redisTemplate.opsForZSet().rangeByScore("testZSet",1,2);
        System.out.println(zmembers.toString());

        //操作hash
        redisTemplate.opsForHash().put("testHash","name","Jack");
        redisTemplate.opsForHash().put("testHash","sex","Male");
        redisTemplate.opsForHash().put("testHash","age","18");

        Map<Object, Object> myHashMap = redisTemplate.opsForHash().entries("testHash");
        System.out.println(myHashMap.toString());
    }
}
