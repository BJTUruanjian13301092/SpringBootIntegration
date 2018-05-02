package com.example.spring_boot_test.data.entity;

import com.example.spring_boot_test.annotation.MyAnnotation;

public class TestAnnotationEntity {

    //fruitType = "Peach"
    @MyAnnotation()
    public String fruitType;
}
