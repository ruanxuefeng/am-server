package com.am.server.api.permission.service.impl;

import com.am.server.api.permission.config.PermissionConfig;
import com.am.server.api.permission.dao.cache.PermissionDAO;
import com.am.server.api.permission.interceptor.annotation.Menu;
import com.am.server.api.permission.interceptor.annotation.Permission;
import com.am.server.api.permission.pojo.po.PermissionTreeDO;
import com.am.server.api.permission.service.PermissionService;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionDAO permissionDAO;

    private final PermissionConfig config;

    public PermissionServiceImpl(PermissionDAO permissionDAO, PermissionConfig config) {
        this.permissionDAO = permissionDAO;
        this.config = config;
    }

    @Override
    public void loadPermissionToCache() {

    }

    private void getPermissionTree() {
        Reflections reflections = new Reflections(config.getBasePackage());
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(Permission.class);
        Map<String, PermissionTreeDO> map = new HashMap<>(8);
        for (Class<?> permissionClass : set) {
            Permission permission = permissionClass.getAnnotation(Permission.class);
            PermissionTreeDO parent = null;
            if (permission.menus().length > 0) {
                for (Menu menu : permission.menus()) {
                    PermissionTreeDO tree = map.get(menu.value());
                    if (tree != null) {
                        // TODO 继续
                    }
                }
            }
        }
    }
}
