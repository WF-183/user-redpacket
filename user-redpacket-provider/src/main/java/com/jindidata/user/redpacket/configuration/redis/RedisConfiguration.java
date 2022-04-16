package com.jindidata.user.redpacket.configuration.redis;

import com.alibaba.fastjson.parser.ParserConfig;
import com.jindidata.core.data.constant.InnerCachedPrefixConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.redis")
@Configuration
public class RedisConfiguration {

    private String host;
    private int port;
    private int database;
    private String password;
    private int readTimeout;
    private int connectTimeout;


    @Bean(name = "redisStandaloneConfiguration")
    JedisConnectionFactory getRedisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password.toCharArray()));
        redisStandaloneConfiguration.setPort(port);
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(connectTimeout));
        jedisClientConfiguration.readTimeout(Duration.ofMillis(readTimeout));
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
        return jedisConnectionFactory;

    }


    @Bean(name = "serializableRedisTemplate")
    public RedisTemplate<Serializable, Serializable> serializableRedisTemplate(RedisConnectionFactory factory) {
        final RedisTemplate<Serializable, Serializable> template = new RedisTemplate<Serializable, Serializable>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(factory);
        return template;
    }


    @Bean("cacheManager")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .entryTtl(Duration.ofSeconds(300))
            .computePrefixWith(name -> name + ":");
        Set<String> cacheNames = new HashSet<>();
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        RedisCacheManager cacheManager = RedisCacheManager
            .builder(connectionFactory)
            .initialCacheNames(cacheNames)
            .withInitialCacheConfigurations(configMap)
            .cacheDefaults(redisCacheConfiguration)
            .build();
        ParserConfig.getGlobalInstance().addAccept("com.jindidata.");
        return cacheManager;
    }

}