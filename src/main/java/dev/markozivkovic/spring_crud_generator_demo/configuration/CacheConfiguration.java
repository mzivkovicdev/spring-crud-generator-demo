package dev.markozivkovic.spring_crud_generator_demo.configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private static final Map<String, Class<?>> TYPED_CACHES = Map.of(
        "productModel", ProductModel.class,
        "orderTable", OrderTable.class,
        "userEntity", UserEntity.class
    );

    @Bean
    @SuppressWarnings("unchecked")
    RedisCacheManager cacheManager(final RedisConnectionFactory factory, @Qualifier("redisObjectMapper") final ObjectMapper redisObjectMapper) {

        final RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeKeysWith(SerializationPair.fromSerializer(RedisSerializer.string()));

        final Map<String, RedisCacheConfiguration> perCache = new HashMap<>();

        TYPED_CACHES.entrySet().forEach(e -> {
            final String cacheName = e.getKey();
            final Class<?> type = e.getValue();

            final RedisSerializer<?> serializer = new JacksonJsonRedisSerializer<>(redisObjectMapper, type);
            final RedisCacheConfiguration cfg = config.serializeValuesWith(
                SerializationPair.fromSerializer((RedisSerializer<Object>) serializer)
            );
            perCache.put(cacheName, cfg);
        });

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(perCache)
                .build();
    }

    @Bean
    ObjectMapper redisObjectMapper() {

        return JsonMapper.builder()
                .changeDefaultVisibility(v -> v.withFieldVisibility(
                    com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY
                ))
                .addModule(new HibernateLazyNullModule())
                .build();
    }
}