package com.chat.project.chat.config;

import com.chat.project.chat.service.CustomerUserDetailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
public class SpringSecurityConfig implements WebMvcConfigurer {

    @Autowired
    private CustomerUserDetailServices userService;
    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 기존 설정 코드...


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8099")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

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

        http
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/unauth/**")
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/",
                                "/signup",
                                "/unauth/**",
                                "/token/**",
                                "/error",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/css/**",
                                "/js/**",
                                "/templates/**",
                                "/logout/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user/**").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) -> formLogin
                        .loginPage("/unauth/login") // 로그인 페이지 URL
                        .loginProcessingUrl("/unauth/login") // 로그인 처리 URL
                        .defaultSuccessUrl("/login/main")
                        .failureHandler(failureHandler())
                        .usernameParameter("email") // 로그인 폼의 사용자 이름 파라미터 이름
                        .passwordParameter("password") // 로그인 폼의 비밀번호 파라미터 이름
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/unauth/login")
                        .invalidateHttpSession(true)//세션 무효화
                        .clearAuthentication(true)//인증 정보 삭제
                        .deleteCookies("JSESSIONID") //쿠키삭제
                        .permitAll()
                );
        http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        )
                );

        return http.build();
    }


    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new CustomAuthFailureHandler();
    }



}

