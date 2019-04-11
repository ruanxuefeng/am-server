package com.am.server.api.admin.user.dao.mongo;

import com.am.server.api.admin.user.entity.UserPermissionCache;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *  用户权限缓存
 * @author 阮雪峰
 * @date 2018/8/2 13:58
 */
@Component
public interface UserPermissionCacheDao extends MongoRepository<UserPermissionCache, Long> {
}
