package com.inhahackathon.foodmarket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.File;
import java.io.IOException;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${resource.file.path}")
    private String filePath;
    @Value("${resource.file.url}")
    private String fileURL;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Resource File Mapped : {} -> {}", fileURL, filePath);
        registry
                .addResourceHandler(fileURL + "/**")
                .addResourceLocations("file://" + filePath + File.separator)
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        System.out.println("resourcePath: " + resourcePath);
                        Resource resource = location.createRelative(resourcePath);
                        if (resource.exists() && resource.isReadable()) {
                            return resource;
                        }
                        return null;
                    }
                });
    }

}
