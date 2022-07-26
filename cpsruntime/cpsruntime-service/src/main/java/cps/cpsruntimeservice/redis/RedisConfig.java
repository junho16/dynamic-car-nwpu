package cps.cpsruntimeservice.redis;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String hostName;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.port}")
    private String port;

    /**
     * 缓存的序列方式自定义设置
     *
     * @param cacheProperties
     * @return
     */
    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().computePrefixWith(name -> name + ":");
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        // 提取配置文件数据
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    /**
     * 指定redis db（当前db为0）
     *
     * @return
     */
    @Bean("redisTemplateDB0")
    public RedisTemplate<String, Object> redisTemplateDB0() {

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(hostName, Convert.toInt(port));
        redisConfig.setPassword(password);
        redisConfig.setDatabase(0);
        RedisTemplate<String, Object> redisTemplate = getStringObjectRedisTemplate(redisConfig);
        return redisTemplate;
    }

    /**
     * 指定redis db（当前db为1）
     *
     * @return
     */
    @Bean("redisTemplateDB1")
    public RedisTemplate<String, Object> redisTemplateDB1() {

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(hostName, Convert.toInt(port));
        redisConfig.setPassword(password);
        redisConfig.setDatabase(1);
        RedisTemplate<String, Object> redisTemplate = getStringObjectRedisTemplate(redisConfig);
        return redisTemplate;
    }

    /**
     * 指定redis db（当前db为2）
     *
     * @return
     */
    @Bean("redisTemplateDB2")
    public RedisTemplate<String, Object> redisTemplateDB2() {

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(hostName, Convert.toInt(port));
        redisConfig.setPassword(password);
        redisConfig.setDatabase(2);
        RedisTemplate<String, Object> redisTemplate = getStringObjectRedisTemplate(redisConfig);
        return redisTemplate;
    }

    private RedisTemplate<String, Object> getStringObjectRedisTemplate(RedisStandaloneConfiguration redisConfig) {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfig);
        lettuceConnectionFactory.afterPropertiesSet();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        //redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        //redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer jacksonSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

}
