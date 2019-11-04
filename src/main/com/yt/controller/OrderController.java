package com.yt.controller;

import com.yt.anno.MyController;
import com.yt.anno.MyQualifer;
import com.yt.anno.MyRequestMapping;
import com.yt.anno.MyRequestParam;
import com.yt.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@MyController
@MyRequestMapping("/order")
public class OrderController {

    @MyQualifer("orderService")
    private OrderService orderService;

    @MyRequestMapping("/create")
    public void createOrder(HttpServletResponse response, @MyRequestParam("userName") String userName, @MyRequestParam("orderId") String orderId) throws IOException {
        PrintWriter writer = response.getWriter();
        String result = orderService.createOrder(userName, orderId);
        writer.write(result);
    }
}
