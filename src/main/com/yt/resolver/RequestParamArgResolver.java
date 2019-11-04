package com.yt.resolver;

import com.yt.anno.MyRequestParam;
import com.yt.anno.MyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@MyService("requestParamArgResolver")
public class RequestParamArgResolver implements ArguementResolver{
    /**
     * 判断是否为当前需要解析的类
     *
     * @param type
     * @param index
     * @param method
     */
    @Override
    public boolean isSupport(Class<?> type, int index, Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation[] parameterAnnotation = parameterAnnotations[index];
        for (Annotation annotation : parameterAnnotation) {
            if (MyRequestParam.class.isAssignableFrom(annotation.getClass())){
                return true;
            }
        }
        return false;
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
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Annotation[] parameterAnnotation = parameterAnnotations[index];
        for (Annotation annotation : parameterAnnotation) {
            if (MyRequestParam.class.isAssignableFrom(annotation.getClass())){
                MyRequestParam myRequestParam = (MyRequestParam) annotation;
                String value = myRequestParam.value();
                String parameter = request.getParameter(value);
                return parameter;
            }
        }
        return null;
    }
}
