package test;

import com.example.spring_boot_test.SpringBootTestApplication;
import com.example.spring_boot_test.service.LoadConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootTestApplication.class)
public class ConfigTest {

    @Autowired
    LoadConfigService loadConfigService;

    @Test
    public void testConfig(){
        loadConfigService.loadConfig();
        loadConfigService.loadConfig2();
    }
}
