package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	 public static final String AUTHORIZATION_HEADER = "Authorization";
	 private String[] DEFAULT_EXCLUDE_PATTERN = {"/user/login", "/user/register"};

    private ApiKey apiKey(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                "Ecomerce REST APIs",
                "Spring Boot API Documentation",
                "2",
                "Terms of service",
                new Contact("Pooja Dabi", "www.s2pedutech.com", "poojadabi@s2pedutech.com"),
                "License of API",
                "API license URL",
                Collections.emptyList()
        );
    }
	@Bean
	public Docket api() {
	return new Docket(DocumentationType.SWAGGER_2)
	.pathMapping("/")
	 .apiInfo(apiInfo())
	 .securityContexts(Arrays.asList(securityContext()))
	 .securitySchemes(Arrays.asList(apiKey()))
	.select()
	.apis(RequestHandlerSelectors.any())
	.paths(PathSelectors.any())
	.build();
	}
	
    private SecurityContext securityContext(){
        Predicate<String> pathsToExclude = path -> Arrays.stream(DEFAULT_EXCLUDE_PATTERN)
                .noneMatch(path::matches);

		return SecurityContext.builder().securityReferences(defaultAuth())
		.forPaths(pathsToExclude) // Use the custom predicate
		.build();
    }

    private List<SecurityReference> defaultAuth(){
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
