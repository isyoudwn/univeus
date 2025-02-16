package com.example.univeus.common.config;

import com.example.univeus.common.resolver.AuthenticationResolver;
import com.example.univeus.common.resolver.MainPageSearchConditionResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationResolver authenticationResolver;
    private final MainPageSearchConditionResolver mainPageSearchConditionResolver;

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationResolver);
        resolvers.add(mainPageSearchConditionResolver);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS 정책 설정 (도메인과 credentials을 허용)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 클라이언트의 origin
                .allowCredentials(true) // credentials(쿠키 등)을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600); // preflight 요청 캐시 시간 설정
    }
}

