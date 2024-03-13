package com.chat.project.chat.config;

import com.chat.project.chat.service.UserDetailServices;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.filter.CorsFilter;
import org.springframework.http.HttpMethod;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private UserDetailServices userService;

    // 스프링 시큐리티 기능 비활성화
/*
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                //.requestMatchers(toH2Console())
                .requestMatchers("/templates/**")
                .requestMatchers("/static/**");
    }
*/


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Swagger 관련 URI 패턴
        String[] SWAGGER_URI = {
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.index.html",
                "/webjars/**",
                "/swagger-resources/**"
        };

        // http 시큐리티 빌더
        http
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/api/**", "/api/token/**")
                ) // csrf를 비활성화할 경로 설정
                .httpBasic(withDefaults()) // 기본 http 인증 사용하지 않음
                // 세션 사용
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                // 특정 경로에 대한 접근 허용 (인증되지 않은 사용자도 접근 가능)
                .authorizeHttpRequests((authorize) -> authorize
                        //.requestMatchers(SWAGGER_URI).permitAll()
                        .requestMatchers("/login","signup","/member").permitAll()
                        .requestMatchers(
                                "/",
                                "/unauth/**",
                                "/token/**",
                                "/error",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/js/**",
                                "/templates/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user/**").hasAuthority("ROLE_USER")
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/mainpage")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());
        http
                // 인증되지 않은 요청에 대한 처리 (401 Unauthorized 응답)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        )
                );

        return http.build();
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

