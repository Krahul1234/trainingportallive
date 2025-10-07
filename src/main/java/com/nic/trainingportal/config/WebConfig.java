package com.nic.trainingportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("https://areaofficer.nic.in",
//                               "http://localhost:4200")
//                .allowedMethods("GET", "POST")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
}
