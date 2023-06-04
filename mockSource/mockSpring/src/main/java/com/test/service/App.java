package com.test.service;

import com.test.spring.ZhouyuApplicationContext;

/**
 * Hello world!
 */
public class App {

    /**
     * Hello world!
     */
    public static void main(String[] args) {

        ZhouyuApplicationContext context = new ZhouyuApplicationContext(AppConfig.class);

        UserService userService = (UserService) context.getBean("userService");
        UserService userService2 = (UserService) context.getBean("userService");
        UserService userService3 = (UserService) context.getBean("userService");

        System.out.println(userService);
        System.out.println(userService2);
        System.out.println(userService3);

        userService.print();

        OrderService orderService = (OrderService) context.getBean("orderService");
        OrderService orderService2 = (OrderService) context.getBean("orderService");
        OrderService orderService3 = (OrderService) context.getBean("orderService");

        System.out.println(orderService);
        System.out.println(orderService2);
        System.out.println(orderService3);
    }

}
