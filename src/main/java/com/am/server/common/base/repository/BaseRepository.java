package com.am.server.common.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/17 11:02
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T,Long>, JpaSpecificationExecutor<T> {
}
