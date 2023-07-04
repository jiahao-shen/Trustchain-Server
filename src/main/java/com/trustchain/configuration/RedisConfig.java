package com.trustchain.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String hostName;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.database}")
    private int database;

    private RedisConnectionFactory factory = null;

    @Bean("redisConnectionFactory")
    public RedisConnectionFactory initConnectionFactory() {
        if (factory != null) {
            return factory;
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxIdle(10);

        poolConfig.setMaxTotal(25);

        poolConfig.setMaxWaitMillis(timeout);

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);

        RedisStandaloneConfiguration rsConfig = jedisConnectionFactory.getStandaloneConfiguration();
        rsConfig.setHostName(hostName);
        rsConfig.setPort(port);
        rsConfig.setPassword(password);
        rsConfig.setDatabase(database);
        factory = jedisConnectionFactory;
        return factory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

}
