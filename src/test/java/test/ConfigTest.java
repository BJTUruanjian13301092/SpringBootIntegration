package test;

import com.example.spring_boot_test.SpringBootTestApplication;
import com.example.spring_boot_test.data.dao.UserMapper;
import com.example.spring_boot_test.data.entity.TestAnnotationEntity;
import com.example.spring_boot_test.data.entity.User;
import com.example.spring_boot_test.data.response.ResponseDetailEntity;
import com.example.spring_boot_test.data.response.UserDetail;
import com.example.spring_boot_test.service.LoadConfigService;
import com.example.spring_boot_test.service.TestAnnotationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    public void testGeneric(){
        //泛型
        ResponseDetailEntity<UserDetail> responseDetailEntity = new ResponseDetailEntity<UserDetail>();
        UserDetail userDetail = new UserDetail();
        userDetail.setId(Long.valueOf("1010"));
        userDetail.setAge(18);
        userDetail.setName("Larry");
        userDetail.setSex(Byte.valueOf("1"));
        responseDetailEntity.setData(userDetail);
        System.out.println("haha");
    }

    @Test
    public void testLambdaExpression(){
        //java8 lambda表达式
        List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.","Canada");
        String G7Countries = G7.stream().map(x -> transformStr(x)).collect(Collectors.joining(", "));
        System.out.println(G7Countries);
    }

    public String transformStr(String str){
        return str + "~";
    }
}
