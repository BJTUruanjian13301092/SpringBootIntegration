package com.example.spring_boot_test.service;

import com.example.spring_boot_test.annotation.MyAnnotation;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class TestAnnotationService {
    public void getInfo(Class<?> clazz){
        for(Field field : clazz.getDeclaredFields()){
            if(field.isAnnotationPresent(MyAnnotation.class)){
                MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);
                String fruitType = annotation.fruitType();
                System.out.println("The type of the fruit is: " + fruitType);
            }
        }
    }
}
