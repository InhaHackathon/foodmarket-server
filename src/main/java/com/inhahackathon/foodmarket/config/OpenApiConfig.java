package com.inhahackathon.foodmarket.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Autowired
    private Environment env;

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info().title("InhaHackathon").version(appVersion)
                .description("FoodMarket API Document")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact().name("한관희").url("https://github.com/limehee").email("noop103@naver.com"))
                .license(new License().name("Food Market License Version 1.0").url("https://github.com/InhaHackathon/FoodMarketServer"));

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(info);
    }

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
                Parameter parameter = new Parameter()
                        .in("header")
                        .name("Authorization")
                        .schema(new io.swagger.v3.oas.models.media.Schema().type("string"));

                operation.addParametersItem(parameter);
            }
            return operation;
        };
    }

}
