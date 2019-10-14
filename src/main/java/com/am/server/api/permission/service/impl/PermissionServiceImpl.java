package com.am.server.api.permission.service.impl;

import com.am.server.api.permission.config.PermissionConfig;
import com.am.server.api.permission.dao.cache.PermissionDAO;
import com.am.server.api.permission.annotation.Menu;
import com.am.server.api.permission.annotation.Permission;
import com.am.server.api.permission.pojo.po.PermissionTreeDO;
import com.am.server.api.permission.service.PermissionService;
import com.am.server.api.role.pojo.vo.PermissionTreeVO;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2019年10月14日13:23:18
 */
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
        permissionDAO.save(getPermissionTree());
    }

    @Override
    public List<PermissionTreeVO> findAll() {
        List<PermissionTreeVO> list = new ArrayList<>();
        Optional.ofNullable(permissionDAO.findAll())
                .ifPresent(permissionTreeList -> permissionTreeList.forEach(permissionTree -> list.add(mapToVO(permissionTree))));
        return list;
    }

    private PermissionTreeVO mapToVO(PermissionTreeDO permissionTree) {
        PermissionTreeVO permissionTreeVO = new PermissionTreeVO()
                .setName(permissionTree.getName())
                .setMark(permissionTree.getMark());
        List<PermissionTreeVO> children = null;
        if (permissionTree.getChildren() != null && permissionTree.getChildren().size() > 0) {
            children = new ArrayList<>();
            for (PermissionTreeDO child : permissionTree.getChildren()) {
                children.add(mapToVO(child));
            }
        }
        Optional.ofNullable(children).ifPresent(permissionTreeVO::setChildren);
        return permissionTreeVO;
    }

    private TreeSet<PermissionTreeDO> getPermissionTree() {
        Reflections reflections = new Reflections(config.getBasePackage(), TypeAnnotationsScanner.class, MethodAnnotationsScanner.class);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Permission.class);
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Permission.class);
        Map<String, PermissionTreeDO> map = new HashMap<>(8);
        TreeSet<PermissionTreeDO> set = new TreeSet<>();
        for (Class<?> permissionClass : classSet) {
            Permission permission = permissionClass.getAnnotation(Permission.class);
            if (permission.check()) {
                PermissionTreeDO parent = null;

                if (permission.menus().length > 0) {
                    //遍历菜单，作为树形结构上级
                    for (Menu menu : permission.menus()) {
                        parent = operate(menu.value(), menu.name(), menu.sort(), map, set, parent);
                    }
                }

                PermissionTreeDO tree = operate(permission.value(), permission.name(), permission.sort(), map, set, parent);
                //找此类上加权限的方法
                Set<Method> classMethodSet = methods.stream()
                        .filter(method -> method.getDeclaringClass().equals(permissionClass))
                        .collect(Collectors.toSet());

                //遍历方法
                for (Method method : classMethodSet) {
                    Permission methodPermission = method.getAnnotation(Permission.class);
                    if (methodPermission.check()) {
                        tree.getChildren().add(
                                new PermissionTreeDO()
                                        .setMark(methodPermission.value())
                                        .setName(methodPermission.name())
                                        .setSort(methodPermission.sort())
                        );
                    }
                }
            }
        }

        return set;
    }

    private PermissionTreeDO operate(String mark, String name, Integer sort, Map<String, PermissionTreeDO> permissionMap,
                                     TreeSet<PermissionTreeDO> rootList, PermissionTreeDO parent) {
        PermissionTreeDO tree = permissionMap.merge(
                mark,
                new PermissionTreeDO()
                        .setMark(mark)
                        .setName(name)
                        .setSort(sort)
                        .setChildren(new TreeSet<>()),
                (oldValue, newValue) -> oldValue
        );
        Optional.ofNullable(parent).ifPresent(p -> p.getChildren().add(tree));
        if (parent == null && tree.getChildren().size() == 0) {
            rootList.add(tree);
        }
        return tree;
    }
}
