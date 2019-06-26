## 项目介绍
    简单的后台管理系统。
    - SpringBoot + Mybatis + Druid + MySQL + MongoDB（详见master分支）
    - SpringBoot + Spring-data-jpa + queryDSL + Druid + MariaDB + MongoDB（存放操作日志） + Redis（缓存用户权限）
    - JDK1.8+
    - 实现功能：用户管理、角色管理、菜单管理、日志管理、公告管理（websocket实现，对登录用户推送，没有实现点对点和选择性推送）
    
### 组织结构

``` lua
    |-am-server
    |-java
    |   |-com.am.server
    |       |-advice 
    |           |-controller    -- 全局异常处理
    |           |-update        -- 更新相关操作（现阶段主要是写入创建人、创建时间）
    |       |-api       -- 所有的请求
    |           |-admin     -- 后台管理相关接口
    |               |-*     -- 相关功能
    |                   |-*     -- 主要包括相关功能的controller、service、dao、entity、config等
    |           |-app       -- app相关接口
    |       |-common        -- 项目的公共模块
    |           |-annotation.transaction        -- 事务相关注解（Commit：写事务，ReadOnly：只读事务）
    |           |-base      -- 各层的基类
    |           |util       -- 工具包
    |       |-config        -- 配置包
    |-resource
    |   |-i18n                  -- 国际化
    |   |-application.yml       -- 基本配置文件
    |   |-application-dev.yml   -- 开发环境配置文件
    |   |-application-pro.yml   -- 生产配置文件
    |   |-banner.txt            -- 自定义启动banner
    |   |-logback-spring.xml    -- 日志配置
```
### 项目特点
    - 基本遵循阿里开发手册
    - 功能少，基本框架，想怎么改就怎么改

配备前端框架：[am-admin](https://gitee.com/ruanxuefeng/am-admin)，在[vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)基础上开发
