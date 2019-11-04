package com.yt.hand;

import com.yt.anno.MyService;
import com.yt.resolver.ArguementResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
@MyService("handToolsService")
public class HandToolsServiceImpl implements HandToolsService {

    @Override
    public Object[] hand(HttpServletRequest request, HttpServletResponse response,
                         Method method, Map<String, Object> beans) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        //获取所有实现了ArgumentResolver的接口实现
        Map<String, Object> instanceType = getInstanceType(beans, ArguementResolver.class);

        int index = 0;
        int i = 0;
        for (Class<?> parameterType : parameterTypes) {
            for (Map.Entry<String, Object> entry : instanceType.entrySet()) {
                ArguementResolver arguementResolver = (ArguementResolver)entry.getValue();
                if (arguementResolver.isSupport(parameterType, index, method)){
                    args[i++] = arguementResolver.resolver(request,response,parameterType,index,method);
                }
            }
            index++;
        }

        return args;
    }

    private Map getInstanceType(Map<String, Object> beans, Class<ArguementResolver> arguementResolverClass) {
        Map<String, Object> resultBeans = new HashMap<>();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Class<?>[] interfaces = entry.getValue().getClass().getInterfaces();
            if (interfaces != null && interfaces.length >0 ){
                for (Class<?> anInterface : interfaces) {
                    if (anInterface.isAssignableFrom(arguementResolverClass)){
                        resultBeans.put(entry.getKey(),entry.getValue());
                    }
                }
            }
        }
        return resultBeans;
    }
}
