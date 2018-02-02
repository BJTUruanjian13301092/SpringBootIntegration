package com.example.spring_boot_test.service;

import com.example.spring_boot_test.lib_svm.svm_predict;
import com.example.spring_boot_test.lib_svm.svm_train;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TestLibSVM {

    public void testSVM() throws IOException{

        String[] arg = { "D:\\projects\\spring_boot_test\\src\\main\\resources\\files\\data_train.txt", // 存放SVM训练模型用的数据的路径
                "D:\\projects\\spring_boot_test\\src\\main\\resources\\files\\data_model.txt" };        // 存放SVM通过训练数据训练出来的模型的路径

        String[] parg = { "D:\\projects\\spring_boot_test\\src\\main\\resources\\files\\data_test.txt", // 这个是存放测试数据
                "D:\\projects\\spring_boot_test\\src\\main\\resources\\files\\data_model.txt",          // 调用的是训练以后的模型
                "D:\\projects\\spring_boot_test\\src\\main\\resources\\files\\data_output.txt" };       // 生成的结果的文件的路径

        System.out.println("........SVM运行开始..........");
        // 创建一个训练对象
        svm_train t = new svm_train();
        // 创建一个预测或者分类的对象
        svm_predict p = new svm_predict();
        t.main(arg); // 调用
        p.main(parg); // 调用
    }
}
