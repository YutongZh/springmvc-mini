package com.yt.resolver;

import com.yt.anno.MyService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@MyService("httpServletRequestResolver")
public class HttpServletRequestResolver implements ArguementResolver{
    /**
     * 判断是否为当前需要解析的类
     *
     * @param type
     * @param index
     * @param method
     */
    @Override
    public boolean isSupport(Class<?> type, int index, Method method) {
        return ServletRequest.class.isAssignableFrom(type);
    }

    /**
     * 参数解析
     *
     * @param request
     * @param response
     * @param type
     * @param index
     * @param method
     */
    @Override
    public Object resolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int index, Method method) {
        return request;
    }
}
