package com.yt.servlet;

import com.yt.anno.MyController;
import com.yt.anno.MyQualifer;
import com.yt.anno.MyRequestMapping;
import com.yt.anno.MyService;
import com.yt.hand.HandToolsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    //类文件路径集合
    private List<String> classNames = new ArrayList<>();

    //ioc容器
    private Map<String, Object> ioc = new HashMap<>();

    //获取url和方法的映射关系
    private Map<String, Object> handMap = new HashMap<>();

    public DispatcherServlet(){}

    //启动tomcat加载
    public void init(ServletConfig servletConfig){

        //扫描哪些类需要被实例化 class（包及包以下的class）
        doScanPackage("com.yt");
        for (String className : classNames) {
            System.out.println(className);
        }

        //实例化所有的类
        doInstance();
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            System.out.println("ioc====" + entry.getKey() + "=====" + entry.getValue());
        }

        //依赖注入
        doDI();

        //建立url与method的映射关系
        handMapper();
        for (Map.Entry<String, Object> stringObjectEntry : handMap.entrySet()) {
            System.out.println(stringObjectEntry.getKey() + "==========" + stringObjectEntry.getValue());
        }
    }

    private void handMapper() {
        if (ioc.entrySet().size() < 0){
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Object instance = entry.getValue();
            Class<?> instanceClass = instance.getClass();
            if (instanceClass.isAnnotationPresent(MyRequestMapping.class)){
                MyRequestMapping controllerMapping = instanceClass.getAnnotation(MyRequestMapping.class);
                String classPath = controllerMapping.value();
                Method[] methods = instanceClass.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(MyRequestMapping.class)) {
                        MyRequestMapping methodMapping = method.getAnnotation(MyRequestMapping.class);
                        String methodPath = methodMapping.value();
                        handMap.put(classPath + methodPath, method);
                    }
                }
            }
        }
    }

    private void doDI() {
        if (ioc.entrySet().size() < 0){
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(MyController.class)){
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    if (declaredField.isAnnotationPresent(MyQualifer.class)){
                        MyQualifer annotation = declaredField.getAnnotation(MyQualifer.class);
                        String value = annotation.value();
                        declaredField.setAccessible(true); //放开权限
                        try {
                            declaredField.set(instance, ioc.get(value));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            } else {
                continue;
            }
        }
    }

    private void doInstance(){
        if (classNames.size() < 0){
            return;
        }

        for (String className : classNames) {
            className = className.replaceAll(".class", "");
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MyController.class)){
                    Object instance = clazz.newInstance();
                    MyRequestMapping myRequestMapping = clazz.getAnnotation(MyRequestMapping.class);
                    String value = myRequestMapping.value().replaceAll("/", "");
                    ioc.put(value, instance);
                } else if (clazz.isAnnotationPresent(MyService.class)){
                    Object instance = clazz.newInstance();
                    MyService annotation = clazz.getAnnotation(MyService.class);
                    String value = annotation.value().replaceAll("/", "");
                    ioc.put(value, instance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void doScanPackage(String basePackage) {
        URL resource = this.getClass().getClassLoader().getResource("/" + basePackage.replaceAll("\\.", "/"));
        String pathName = resource.getFile();
        File file = new File(pathName);
        String[] fileList = file.list();
        for (String path : fileList) {
            File filePath = new File(pathName + path);  //找到磁盘路径 + com.yt
            if (filePath.isDirectory()){
                doScanPackage(basePackage + "." + path);
            } else {
                classNames.add(basePackage + "." + filePath.getName());
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI(); //请求路径
        //String contextPath = req.getContextPath(); //springmvc-mini
        //String path = requestURI.replaceAll(contextPath, "");
        Method method = (Method) handMap.get(requestURI);
        Object instance = ioc.get(requestURI.split("/")[1]);
        HandToolsService handToolsService = (HandToolsService)ioc.get("handToolsService");
        Object[] args = handToolsService.hand(req, resp, method, ioc);
        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
