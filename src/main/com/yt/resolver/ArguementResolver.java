package com.yt.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public interface ArguementResolver {

    /**
     * 判断是否为当前需要解析的类
     */
     boolean isSupport(Class<?> type, int index, Method method);

    /**
     * 参数解析
     */
     Object resolver(HttpServletRequest request, HttpServletResponse response,
                           Class<?> type, int index, Method method);
}
