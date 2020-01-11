package com.atguigu;

import com.atguigu.pojo.User;
import com.atguigu.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;


import javax.annotation.Resource;

/**
 * @author wxy
 * @create 2020-01-09 11:59
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EsDemoTest {
    @Resource
    private UserRepository userRepository;
    @Resource
    private ElasticsearchRestTemplate restTemplate;
   @Test
    public  void test(){
          this.restTemplate.createIndex(User.class);
          this.restTemplate.putMapping(User.class);
   }
   @Test
    public  void testDocument(){
      //this.userRepository.save(new User(1l,"王雪艳",10,"123456"));
       List<User> users = new ArrayList<>();
       users.add(new User(1l, "柳岩", 18, "123456"));
       users.add(new User(2l, "范冰冰", 19, "123456"));
       users.add(new User(3l, "李冰冰", 20, "123456"));
       users.add(new User(4l, "锋哥", 21, "123456"));
       users.add(new User(5l, "小鹿", 22, "123456"));
       users.add(new User(6l, "韩红", 23, "123456"));
       this.userRepository.saveAll(users);

   }
@Test
 public  void  testQuery(){
  //  this.userRepository.findByAgeBetween(18,20);
    //this.userRepository.findByNative(19,22).forEach(System.out::println);
    }
}
