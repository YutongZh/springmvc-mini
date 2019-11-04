package com.yt.resolver;

import com.yt.anno.MyService;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@MyService("httpServletResponseResolver")
public class HttpServletResponseResolver implements ArguementResolver{
    /**
     * 判断是否为当前需要解析的类
     *
     * @param type
     * @param index
     * @param method
     */
    @Override
    public boolean isSupport(Class<?> type, int index, Method method) {
        return ServletResponse.class.isAssignableFrom(type);
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
        return response;
    }
}
