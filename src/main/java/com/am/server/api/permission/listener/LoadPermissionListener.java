package com.am.server.api.permission.listener;

import com.am.server.api.permission.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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
