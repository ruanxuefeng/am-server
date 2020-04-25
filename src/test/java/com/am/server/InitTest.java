package com.am.server;

import com.am.server.api.permission.service.PermissionService;
import com.am.server.api.role.dao.rdb.RoleDao;
import com.am.server.api.role.pojo.po.RoleDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class InitTest {

    @Autowired
    RoleDao roleDao;

    @Autowired
    PermissionService permissionService;

    @Rollback(false)
    @Test
    public void setPermission() {
        roleDao.findById(940823091829805056L)
                .ifPresent(role->{
                    Set<String> set = permissionService.findAllPermissionRemarkList();
                    List<String> permissionList = new ArrayList<>(set);
                    role.setPermissions(permissionList);
                    roleDao.save(role);
                });
    }
}
