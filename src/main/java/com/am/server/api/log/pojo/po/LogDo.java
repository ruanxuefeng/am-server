package com.am.server.api.log.pojo.po;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author 阮雪峰
 * @date 2019年6月25日15:23:41
 */
@Document("log")
@Accessors(chain = true)
@Data
public class LogDo {
    @Id
    private Long id;

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

    private LocalDateTime createTime;
}
