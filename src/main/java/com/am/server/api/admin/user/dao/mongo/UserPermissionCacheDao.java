package com.am.server.api.admin.user.dao.mongo;

import com.am.server.api.admin.user.pojo.po.UserPermissionCachePO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 *  用户权限缓存
 * @author 阮雪峰
 * @date 2018/8/2 13:58
 */
@Component
public interface UserPermissionCacheDao extends MongoRepository<UserPermissionCachePO, Long> {
}
