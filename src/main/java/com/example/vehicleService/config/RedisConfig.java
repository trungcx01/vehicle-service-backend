package com.example.vehicleService.config;

import com.example.vehicleService.service.LiveTrackingSubcriber;
import com.example.vehicleService.service.RedisPubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


//@Bean: Khởi tạo Bean và quản lý nó trong Application Context
// Redis lưu trữ dữ liệu dưới dạng nhị phân ==> cần serializer
@Configuration
public class RedisConfig {
    @Autowired
    private LiveTrackingSubcriber liveTrackingSubcriber;
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory();
    }

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);  //tự động set LettuceConnectionFactory
//        redisTemplate.setKeySerializer(new StringRedisSerializer());  // chuyển key về dạng string
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());  // chuyển value về dạng Json
//        return redisTemplate;
//    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory connectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(redisPubSubService, new PatternTopic("emergency-request"));
//        container.addMessageListener(redisPubSubService, new PatternTopic("user-*"));
        container.addMessageListener(new MessageListenerAdapter(liveTrackingSubcriber, "onMessage"), new PatternTopic("live-tracking"));
        return container;
    }

    @Bean
    public MessageListenerAdapter liveTrackingListenerApdaper(LiveTrackingSubcriber subcriber){
        return new MessageListenerAdapter(subcriber, "onMessage");
    }
}
