package com.flakesoup.auth.test;


import com.flakesoup.auth.FlakeSoupAuthApplication;
import com.flakesoup.auth.entity.User;
import com.flakesoup.auth.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FlakeSoupAuthApplication.class)
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetUserByName(){
        User user = userService.getUserByName("marilyn");
        System.out.println(user);
    }

    @Test
    public void testGetUserById(){
        User user = userService.getUserById(31L);
        System.out.println(user);
    }

    @Test
    public void testGetUserByMobile(){
        User user = userService.getUserByMobile("15911137383");
        System.out.println(user);
    }
}
