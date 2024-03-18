package util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.net.URI;

@EnableRedisHttpSession //Spring session이 HTTP 세션을 Redis에 저장하도록 설정.
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.url}")
    private String redisUrl;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        // Redis URL을 파싱하여 호스트와 포트를 추출
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        // URI 객체로 URL을 파싱
        URI redisUri = URI.create(redisUrl);

        configuration.setHostName(redisUri.getHost());
        configuration.setPort(redisUri.getPort());

        // 비밀번호가 있는 경우 설정
        if (redisUri.getUserInfo() != null) {
            configuration.setPassword(RedisPassword.of(redisUri.getUserInfo().split(":", 2)[1]));
        }

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }

}