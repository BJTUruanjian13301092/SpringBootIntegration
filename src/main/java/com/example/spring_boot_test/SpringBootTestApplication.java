package com.example.spring_boot_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching //开启缓存
@EnableTransactionManagement // 开启事务管理
public class SpringBootTestApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringBootTestApplication.class, args);
	}
}
