package com.chat.project.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableJpaAuditing //auditing 기능 위한 추가
@SpringBootApplication
@EnableCaching // 레디스 캐싱 하기위해 추가, @Cacheable 같은 어노테이션을 인식 하게 함
@EnableRedisHttpSession
@ComponentScan(basePackages = {"com.chat.project", "util"}) // 'util' 패키지 포함
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

}
