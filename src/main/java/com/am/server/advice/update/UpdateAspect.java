package com.am.server.advice.update;

import com.am.server.common.base.entity.BaseEntity;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.IdUtils;
import com.am.server.common.util.JwtUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 *  新增、修改
 * @author 阮雪峰
 * @date 2018/9/4 12:52
 */
@Aspect
@Component
public class UpdateAspect {

    private final HttpServletRequest request;

    public UpdateAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Before("@annotation(com.am.server.advice.update.annotation.Save)")
    public void beforeSave(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseEntity) {
                BaseEntity entity = (BaseEntity) arg;
                entity.setId(IdUtils.getId());
                entity.setCreateTime(LocalDateTime.now());
                String token = request.getHeader(Constant.TOKEN);
                if (token != null && !token.isEmpty()) {
                    entity.setCreator(Long.valueOf(JwtUtils.getSubject(token)));
                }
            }
        }
    }

}
