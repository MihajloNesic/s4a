package com.mihajlo.smart4aviation.s4atask.config;

import com.mihajlo.smart4aviation.s4atask.controller.ControllerPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackageClasses = ControllerPackage.class)
public class SpringFoxConfig {

    private static final String APP_TITLE = "Smart4aviation";
    private static final String APP_DESCRIPTION = "Smart4aviation assignment";
    private static final String APP_VERSION = "1.0";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(ControllerPackage.class.getPackageName()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(APP_TITLE)
                .description(APP_DESCRIPTION)
                .version(APP_VERSION)
                .build();
    }
}