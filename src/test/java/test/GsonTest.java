package test;

import com.example.spring_boot_test.entity.User;
import com.example.spring_boot_test.utils.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GsonTest {

    @SuppressWarnings("resource")
    @Test
    public void testParseGson() {

        String json1 = "{\"name\":\"larry\", \"sex\":\"male\", \"age\":\"23\"}";
        String json2 = "[{\"name\":\"euphie\", \"sex\":\"female\", \"age\":\"22\"}, {\"name\":\"larry\", \"sex\":\"male\", \"age\":\"23\"}]";

        User user = GsonUtil.parseJsonWithGson(json1, User.class);
        System.out.println("Name: " + user.getName() + " Age: " + user.getAge() + " Sex: " + user.getSex());

        List<User> listUser;
        listUser = GsonUtil.parseJsonArrayWithGson(json2, User.class);
        System.out.println(listUser.toString());
    }

    @SuppressWarnings("resource")
    @Test
    public void testCreateGson(){

        User user = new User();
        user.setName("james");
        user.setSex("male");
        user.setAge("33");

        Gson gson = new GsonBuilder().create();
        String str = gson.toJson(user);
        System.out.print(str);
    }
}
