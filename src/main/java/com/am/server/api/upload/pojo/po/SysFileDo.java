package com.am.server.api.upload.pojo.po;


import com.am.server.api.upload.enumerate.FileType;
import com.am.server.common.base.pojo.po.BaseDo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统所用到的文件
 *
 * @author 阮雪峰
 * @date 2020年4月25日12:30:32
 */
@Table(name = "sys_file")
@Entity
@Accessors(chain = true)
@Setter
@Getter
public class SysFileDo extends BaseDo {
    /**
     * 类型
     */
    private FileType type;
    /**
     * 网络请求路径
     */
    private String url;
    /**
     * 本地资源访问路径,相对路径
     */
    private String dir;
}
