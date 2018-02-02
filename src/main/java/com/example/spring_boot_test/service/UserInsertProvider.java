package com.example.spring_boot_test.service;

import com.example.spring_boot_test.entity.User;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class UserInsertProvider {

    public String saveAll(Map map){

        List<User> users = (List<User>)map.get("list");
        StringBuilder sb = new StringBuilder();
        sb.append("insert into user");
        sb.append("(name,sex,age)");
        sb.append("values");
        MessageFormat mf = new MessageFormat("#'{'list[{0}].name'}'," +
                                                    "#'{'list[{0}].sex'}'," +
                                                    "#'{'list[{0}].age'}'");
        for(int i=0;i<users.size();i++){
            sb.append("(");
            sb.append(mf.format(new Object[]{i}));
            sb.append(")");

            if(i<users.size()-1){
                sb.append(",");
            }
        }

        return sb.toString();
    }
}