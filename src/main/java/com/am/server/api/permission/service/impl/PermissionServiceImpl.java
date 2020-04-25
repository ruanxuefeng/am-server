package com.am.server.api.permission.service.impl;

import com.am.server.api.permission.config.PermissionConfig;
import com.am.server.api.permission.dao.cache.PermissionDao;
import com.am.server.api.permission.annotation.Menu;
import com.am.server.api.permission.annotation.Permission;
import com.am.server.api.permission.pojo.po.PermissionTreeDo;
import com.am.server.api.permission.service.PermissionService;
import com.am.server.api.role.pojo.vo.PermissionTreeVo;
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

    private final PermissionDao permissionDAO;

    private final PermissionConfig config;

    public PermissionServiceImpl(PermissionDao permissionDAO, PermissionConfig config) {
        this.permissionDAO = permissionDAO;
        this.config = config;
    }

    @Override
    public void loadPermissionToCache() {
        permissionDAO.save(getPermissionTree());
    }

    @Override
    public List<PermissionTreeVo> findAll() {
        List<PermissionTreeVo> list = new ArrayList<>();
        Optional.ofNullable(permissionDAO.findAll())
                .ifPresent(permissionTreeList -> permissionTreeList.forEach(permissionTree -> list.add(mapToVO(permissionTree))));
        return list;
    }

    @Override
    public Set<String> findAllPermissionRemarkList() {
        List<PermissionTreeVo> list = findAll();
        Set<String> set = new HashSet<>();
        setRemark(list, set);
        return set;
    }

    private void setRemark(List<PermissionTreeVo> list, Set<String> set) {
        for (PermissionTreeVo permissionTree : list) {
            set.add(permissionTree.getMark());
            if (permissionTree.getChildren() != null && permissionTree.getChildren().size() > 0) {
                setRemark(permissionTree.getChildren(), set);
            }
        }
    }

    private PermissionTreeVo mapToVO(PermissionTreeDo permissionTree) {
        PermissionTreeVo permissionTreeVO = new PermissionTreeVo()
                .setName(permissionTree.getName())
                .setMark(permissionTree.getMark());
        List<PermissionTreeVo> children = null;
        if (permissionTree.getChildren() != null && permissionTree.getChildren().size() > 0) {
            children = new ArrayList<>();
            for (PermissionTreeDo child : permissionTree.getChildren()) {
                children.add(mapToVO(child));
            }
        }
        Optional.ofNullable(children).ifPresent(permissionTreeVO::setChildren);
        return permissionTreeVO;
    }

    private TreeSet<PermissionTreeDo> getPermissionTree() {
        Reflections reflections = new Reflections(config.getBasePackage(), TypeAnnotationsScanner.class, MethodAnnotationsScanner.class);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Permission.class);
        Set<Method> methods = reflections.getMethodsAnnotatedWith(Permission.class);
        Map<String, PermissionTreeDo> map = new HashMap<>(8);
        TreeSet<PermissionTreeDo> set = new TreeSet<>();
        for (Class<?> permissionClass : classSet) {
            Permission permission = permissionClass.getAnnotation(Permission.class);
            if (permission.check()) {
                PermissionTreeDo parent = null;

                if (permission.menus().length > 0) {
                    //遍历菜单，作为树形结构上级
                    for (Menu menu : permission.menus()) {
                        parent = operate(menu.value(), menu.name(), menu.sort(), map, set, parent);
                    }
                }

                PermissionTreeDo tree = operate(permission.value(), permission.name(), permission.sort(), map, set, parent);
                //找此类上加权限的方法
                Set<Method> classMethodSet = methods.stream()
                        .filter(method -> method.getDeclaringClass().equals(permissionClass))
                        .collect(Collectors.toSet());

                //遍历方法
                for (Method method : classMethodSet) {
                    Permission methodPermission = method.getAnnotation(Permission.class);
                    if (methodPermission.check()) {
                        tree.getChildren().add(
                                new PermissionTreeDo()
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

    private PermissionTreeDo operate(String mark, String name, Integer sort, Map<String, PermissionTreeDo> permissionMap,
                                     TreeSet<PermissionTreeDo> rootList, PermissionTreeDo parent) {
        PermissionTreeDo tree = permissionMap.merge(
                mark,
                new PermissionTreeDo()
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
