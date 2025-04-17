package edu.du.gatewayservice.config;

import edu.du.gatewayservice.interceptor.AdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**", "/api/admin/**")
                .excludePathPatterns("/admin/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 혹은 필요에 따라 "/**"
                .allowedOrigins("http://localhost:5173") // ✅ Vue 포트
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
} 