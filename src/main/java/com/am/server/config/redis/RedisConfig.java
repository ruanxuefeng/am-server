package com.am.server.config.redis;


import com.am.server.api.permission.pojo.po.PermissionTreeDO;
import com.am.server.api.user.pojo.po.UserPermissionDO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author 阮雪峰
 * @date 2019/3/27 13:20
 */
@Configuration
public class RedisConfig {


    /**
     * 如使用注解的话需要配置cacheManager
     * @param connectionFactory connectionFactory
     * @return org.springframework.cache.CacheManager
     * @date 2019/3/28 13:38
     * @author 阮雪峰
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //设置默认超过期时间是1天
        defaultCacheConfig.entryTtl(Duration.ofDays(1));
        //初始化RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }

    @Bean("userPermissionCacheRedisTemplate")
    public RedisTemplate<String, UserPermissionDO> userPermissionCacheRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, UserPermissionDO> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(UserPermissionDO.class);

        return configTemplate(template, serializer);
    }


    @Bean("permissionCacheRedisTemplate")
    public RedisTemplate<String, PermissionTreeDO> permissionCacheRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, PermissionTreeDO> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(PermissionTreeDO.class);

        return configTemplate(template, serializer);
    }

    private <I, T> RedisTemplate<I, T> configTemplate(RedisTemplate<I, T> template, Jackson2JsonRedisSerializer serializer) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

}
