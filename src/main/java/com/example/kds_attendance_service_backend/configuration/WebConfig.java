package com.example.kds_attendance_service_backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;


    // This allows your React frontend to call your backend
    // Without this, browser will block all API calls


    // This tells Spring Boot to serve files from
    // your uploads folder as static resources
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }


}