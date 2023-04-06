package io.wisoft.capstonedesign.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.wisoft.capstonedesign.interceptor.BearerAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final BearerAuthInterceptor bearerAuthInterceptor;
    private final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    public WebMvcConfig(final BearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry){
        logger.info(">>> 인터셉터 등록");

        /* 추후에 등록
        registry.addInterceptor(bearerAuthInterceptor)
                .addPathPatterns("/api/members")
                .addPathPatterns("/api/members/**")
                .excludePathPatterns("/api/members/login")
                .excludePathPatterns("/api/members/signup")

                .addPathPatterns("/api/staff")
                .addPathPatterns("/api/staff/**")
                .excludePathPatterns("/api/staff/login")
                .excludePathPatterns("/api/staff/signup")

                .addPathPatterns("/api/health-infos/new")
                .addPathPatterns("/api/health-infos/new")
        ;

         */
    }
}
