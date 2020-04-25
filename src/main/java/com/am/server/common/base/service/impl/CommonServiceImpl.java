package com.am.server.common.base.service.impl;

import com.am.server.common.base.exception.NoTokenException;
import com.am.server.common.base.pojo.po.BaseDo;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.IdUtils;
import com.am.server.common.util.IpUtils;
import com.am.server.common.util.JwtUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2019年6月25日09:25:04
 */
@Service
public class CommonServiceImpl implements CommonService {
    private final HttpServletRequest request;

    public CommonServiceImpl(HttpServletRequest request) {
        this.request = request;
    }


    @Override
    public Long getLoginUserId() {
        return Optional.ofNullable(request.getHeader(Constant.TOKEN))
                .map(token -> Long.valueOf(JwtUtils.getSubject(token)))
                .orElseThrow(NoTokenException::new);
    }

    @Override
    public String getRequestIp() {
        return IpUtils.getIpAddress(request);
    }

    @Override
    public void beforeSave(BaseDo entity) {
        if (entity.getId() == null) {
            entity.setId(IdUtils.getId());
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedById(getLoginUserId());

        }
        entity.setUpdatedTime(LocalDateTime.now());
        entity.setUpdatedById(getLoginUserId());
    }
}
