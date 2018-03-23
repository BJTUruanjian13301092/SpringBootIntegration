package com.example.spring_boot_test.service;

import com.example.spring_boot_test.entity.User;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class UserProvider {

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
        System.out.println(sb.toString());
        return sb.toString();
    }


    public String saveAllBySQL(Map map){

        List<User> users = (List<User>)map.get("list");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<users.size();i++){
            SQL sql = new SQL();
            sql.INSERT_INTO("user");
            sql.INTO_COLUMNS("name, sex, age");
            sql.INTO_VALUES("'"+users.get(i).getName()+"','"+ users.get(i).getSex()+"','"+users.get(i).getAge()+"'");
            sb.append(sql.toString()+";"+"\n");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public String selectByName(String name){
        return new SQL(){
            {
                SELECT("name, age, sex");
                FROM("user");
                WHERE("name is" + name);
            }
        }.toString();
    }
}