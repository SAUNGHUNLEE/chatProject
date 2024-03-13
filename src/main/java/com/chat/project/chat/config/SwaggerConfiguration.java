package com.chat.project.chat.config;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.OpenAPI;


import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "lsh API",
                version = "1.0.0",
                description = "개인 프로젝트"
        ),
        servers = @Server(url = "/")
)

@RequiredArgsConstructor
public class SwaggerConfiguration {


    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("lsh")
                .packagesToScan("com.chat.project.controller")
                .pathsToMatch("/public/**")
                .build();
    }
    // Swagger 설정을 위한 Docket Bean 정의 -> swagger3에선 GroupedOpenApi로 변경
/*    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // 해당 패키지 내의 API만 문서화
                .apis(RequestHandlerSelectors.basePackage("com.chat.project.controller"))
                // '/public.*' 경로의 API에 대해서만 문서화 진행
                .paths(PathSelectors.regex("/public.*"))
                .build()
                // Swagger 문서의 그룹 이름 설정
                .groupName("lsh")
                // 보안 설정 적용
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                // API 정보 설정
                .apiInfo(apiInfo());
    }*/

    // API 기본 정보 설정 -> OpenAPIDefinition로 대체
/*    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("lsh API")
                .description("개인프로젝트")
                .version("1.0.0")
                .build();
    }*/

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // JWT 보안 요구 사항을 추가합니다. 이는 인증이 필요한 API에 적용됩니다.
                .addSecurityItem(new SecurityRequirement().addList("JWT", new ArrayList<>()))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

/*    // JWT를 사용한 API 보안 스키마 정의 -> customOpenAPI 로 변경
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header Bearer");
    }*/

    // 보안 컨텍스트 설정
/*    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    // Swagger 보안 참조 기본 설정
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }*/

    // Swagger 페이지 정보를 위한 내부 클래스
    @Getter
    @Setter
    @Schema
    static class Page {

        @Schema(description = "페이지 번호")
        private Integer page;

        @Schema(description = "페이지 크기", allowableValues = "range[0, 100]")
        private Integer size;

        @Schema(description = "정렬 방식")
        private List<String> sort;
    }
}