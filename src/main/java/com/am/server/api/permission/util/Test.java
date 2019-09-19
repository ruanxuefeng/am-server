package com.am.server.api.permission.util;

import com.am.server.api.permission.annotation.Permission;
import org.reflections.Reflections;

import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Reflections reflections = new Reflections("com.am.server.api");
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(Permission.class);
        for (Class<?> aClass : set) {
            Permission permission = aClass.getAnnotation(Permission.class);
            System.out.println(permission.name());
        }

    }
}
