
<p align="center">
<img src="https://img.shields.io/badge/SpringBoot-2.2.0-green" alt="springboot" />
<img src="https://img.shields.io/badge/Spring--Data--JPA-2.2.0-orange" alt="mybatis">
<img src="https://img.shields.io/badge/swagger2-2.9.2-bule" alt="swagger2">
<img src="https://img.shields.io/badge/Druid-1.1.13-lightgrey" alt="Druid">
<img src="https://img.shields.io/badge/Mariadb-blue" alt="Druid">
<img src="https://img.shields.io/badge/Redis-red" alt="Druid">
<img src="https://img.shields.io/badge/Mongodb-brightgreen" alt="Druid">
<img src="https://img.shields.io/badge/Docker-blue" alt="Druid">
</p>

## 写在前面
###### 我们推出了企业级后端开发框架[am-admin]([图片]https://gitee.com/ruanxuefeng/am-admin)，更好的为企业用户或个人编码能力较强用户解决快速开发的问题。我们用到redis、mMongodb、，mariadb数据库，针对不同业务场景进行缓存和永久性存储。本开源项目也遵循阿里java开发规范，良好的编码习惯有助于维护和优化。前端采用[vue-element-admin](https://panjiachen.gitee.io/vue-element-admin-site/zh) 开源前端框架，它基于 [vue](https://cn.vuejs.org)（构建数据驱动的 web 界面的渐进式框架），[element-ui](https://element.eleme.cn) （基于 Vue 2.0 的桌面端组件库），并且用到了对于后端工程师比较良好的[typescript](http://www.typescriptlang.org/)，更有助于小团队敏捷开发！
如果你发现本项目有不完善的地方，欢迎提交修复方案，小弟在此谢过。

## 项目介绍
    简单的后台管理系统。
    - SpringBoot + Mybatis + Druid + MySQL + MongoDB（详见master分支）
    - SpringBoot + Spring-data-jpa + queryDSL + Druid + MariaDB + MongoDB（存放操作日志） + Redis（缓存用户权限）
    - JDK1.8+

***

**欢迎你加入我们QQ群854391197，基本没人吱声，也许也没有几个人，如果遇到问题群主都会耐心解答。没有QQ的朋友可以给我发邮件xuefeng.ruan@qq.com**

***

    
### 组织结构

``` lua
    |-am-server
    |-java
    |   |-com.am.server
    |       |-advice 
    |           |-controller    -- 全局异常处理
    |       |-api       -- 所有的请求
    |           |-admin     -- 后台管理相关接口
    |               |-*     -- 相关功能
    |                   |-*     -- 主要包括相关功能的controller、service、dao、pojo、config等
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
### 实现功能
    - 用户管理
    - 角色管理
    - 日志管理
    - 公告管理（websocket实现）
    
### 接口文档
    - 使用swagger生成接口文档，启动服务，访问http://ip:port/doc.html
    
### 项目特点
    - 基本遵循阿里开发手册
    - 功能少，基本框架，想怎么改就怎么改
    - 注释全

配备前端框架：[vue-typescript-element-admin](https://gitee.com/ruanxuefeng/vue-typescript-element-admin)，在[vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)基础上开发

### 项目展示
![登录页面 | center](https://ruanxuefeng.gitee.io/source/am/login.jpg)
![系统首页 | center](https://ruanxuefeng.gitee.io/source/am/dashboard.png)
![用户列表 | center](https://ruanxuefeng.gitee.io/source/am/user-list.png)
![新增用户 | center](https://ruanxuefeng.gitee.io/source/am/user-add.png)
![修改角色 | center](https://ruanxuefeng.gitee.io/source/am/role.png)
