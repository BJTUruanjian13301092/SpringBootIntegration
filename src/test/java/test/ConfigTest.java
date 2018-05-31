package test;

import com.example.spring_boot_test.SpringBootTestApplication;
import com.example.spring_boot_test.data.dao.UserMapper;
import com.example.spring_boot_test.data.entity.TestAnnotationEntity;
import com.example.spring_boot_test.data.entity.User;
import com.example.spring_boot_test.service.LoadConfigService;
import com.example.spring_boot_test.service.TestAnnotationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootTestApplication.class)
public class ConfigTest {

    @Autowired
    LoadConfigService loadConfigService;

    @Autowired
    TestAnnotationService testAnnotationService;

    @Autowired
    UserMapper userMapper;

    @Test
    public void testConfig(){
        loadConfigService.loadConfig();
        loadConfigService.loadConfig2();
    }

    @Test
    public void testAnnotation(){
        testAnnotationService.getInfo(TestAnnotationEntity.class);
    }

    @Test
    public void testMapper(){
        List<User> userList = userMapper.findUsefulUser(null);
        System.out.println("haha");
    }
}
