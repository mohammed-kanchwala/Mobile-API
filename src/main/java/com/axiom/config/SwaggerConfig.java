package com.axiom.config;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.axiom.util.ApiConstants;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean enableSwagger;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(ApiConstants.SWAGGER_PACKAGE)).paths(PathSelectors.any())
                .build().apiInfo(apiInfo()).enable(enableSwagger);

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(ApiConstants.SWAGGER_API_TITLE).description(ApiConstants.API_DETAILS).version(ApiConstants.SWAGGER_VERSION).build();
    }
}
