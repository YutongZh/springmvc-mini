package com.yt.service;

import com.yt.anno.MyService;

@MyService("orderService")
public class OrderServiceImpl implements OrderService {

    @Override
    public String createOrder(String userName, String orderId) {
        return "this is " + userName + ";orderId=" + orderId;
    }
}
