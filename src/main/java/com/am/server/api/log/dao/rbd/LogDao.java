package com.am.server.api.log.dao.rbd;

import com.am.server.api.log.pojo.ao.LogListAo;
import com.am.server.api.log.pojo.po.LogDo;
import org.springframework.data.domain.Page;

/**
 * @author 阮雪峰
 * @date 2018/8/1 14:37
 */
public interface LogDao {
    /**
     * 新增
     *
     * @param log log
     */
    void save(LogDo log);

    /**
     * 分页查询
     *
     * @param logListAo logListAo
     * @return Page<LogDo>
     */
    Page<LogDo> findAll(LogListAo logListAo);
}
