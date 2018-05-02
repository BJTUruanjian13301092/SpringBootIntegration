package com.example.spring_boot_test.service;

import com.example.spring_boot_test.scala.MySpark;
import org.springframework.stereotype.Service;

@Service
public class ScalaService {

    public void sparkTest(){
        int[] sparkArray = MySpark.sparkTest();
        for(int integer : sparkArray){
            System.out.println(integer);
        }
    }

    public static String useJava(){
        return "Scala use Java here ~";
    }
}
