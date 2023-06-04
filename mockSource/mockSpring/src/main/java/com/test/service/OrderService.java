package com.test.service;

import com.test.spring.Component;
import com.test.spring.Scope;

@Component("orderService")
@Scope("prototype")
public class OrderService {

    public void print()
    {
        System.out.println("OrderService::print");
    }
}
