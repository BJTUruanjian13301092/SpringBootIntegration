package com.example.spring_boot_test.service;

import com.example.spring_boot_test.dao.TestInterface;
import org.springframework.stereotype.Service;

@Service
public class TestInterfaceService implements TestInterface {

    public void testMyInterface(){
        System.out.println("Implements Success !");
    }
}
