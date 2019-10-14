package com.am.server.api.permission.listener;

import com.am.server.api.permission.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author 阮雪峰
 * @date 2019-10-14 13:22:34
 */
@Slf4j
@Component
public class LoadPermissionListener implements ApplicationListener<ContextRefreshedEvent> {

    private final PermissionService permissionService;

    public LoadPermissionListener(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        permissionService.loadPermissionToCache();
    }
}
