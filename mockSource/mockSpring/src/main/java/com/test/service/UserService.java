package com.test.service;


import com.test.spring.Autowired;
import com.test.spring.Component;
import com.test.spring.InitializingBean;

@Component("userService")
public class UserService implements InitializingBean {

    @Autowired
    OrderService orderService;

    public void print() {
        System.out.println("UserService::print");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("UserService::afterPropertiesSet");

    }
}
