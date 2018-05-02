package test;

import com.example.spring_boot_test.SpringBootTestApplication;
import com.example.spring_boot_test.service.ScalaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootTestApplication.class)
public class SparkTest {

    @Autowired
    ScalaService scalaService;

    @Test
    public void myTest(){
        scalaService.sparkTest();
    }
}
