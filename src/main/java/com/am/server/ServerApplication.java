package com.am.server;

import com.spring4all.mongodb.EnableMongoPlus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author 阮雪峰
 * @date 2018/6/25 16:04
 */
@EnableJpaRepositories(basePackages = {"com.am.server.api.*.dao.rdb"})
@EnableMongoRepositories(basePackages = "com.am.server.api.*.dao.nosql")
@EnableRedisRepositories(basePackages = "com.am.server.api.*.dao.cache")
@SpringBootApplication
@EnableTransactionManagement
@EnableMongoPlus
@EnableScheduling
@EnableConfigurationProperties
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
