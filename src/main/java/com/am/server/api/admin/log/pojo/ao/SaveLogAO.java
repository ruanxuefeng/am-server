package com.am.server.api.admin.log.pojo.ao;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 阮雪峰
 * @date 2019年6月25日15:24:43
 */
@ApiModel
@Accessors(chain = true)
@Data
public class SaveLogAO {
    /**
     * 操作人
     */
    private String name;

    /**
     * 菜单
     */
    private String menu;

    /**
     * 操作
     */
    private String operate;

    /**
     * ip地址
     */
    private String ip;
}
