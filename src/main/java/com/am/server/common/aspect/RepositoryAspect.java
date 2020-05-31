package com.am.server.common.aspect;

import com.am.server.common.base.pojo.po.BaseDo;
import com.am.server.common.base.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 新增修改切面
 *
 * @author 阮雪峰
 * @date 2020年5月30日11:31:47
 */
@Slf4j
@Aspect
@Component
public class RepositoryAspect {

    private final CommonService commonService;

    public RepositoryAspect(CommonService commonService) {
        this.commonService = commonService;
    }

    @Before("execution(* com.am.server.api.*.repository.*Repository.save(*))")
    public void beforeSave(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseDo) {
                BaseDo param = (BaseDo) arg;
                commonService.beforeSave(param);
            }
        }
    }
}
