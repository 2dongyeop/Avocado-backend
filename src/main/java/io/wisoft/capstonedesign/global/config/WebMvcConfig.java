package io.wisoft.capstonedesign.global.config;

import io.wisoft.capstonedesign.global.interceptor.BearerAuthInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry){
        logger.info("인터셉터 등록");

        registry.addInterceptor(bearerAuthInterceptor)

                .addPathPatterns("/api/auth/logout")
                .addPathPatterns("/jwt/re-issuance")

                .addPathPatterns("/api/members")
                .addPathPatterns("/api/members/**") //마이페이지가 여기에 같이 포함

                .addPathPatterns("/api/staff")
                .addPathPatterns("/api/staff/**")

                .addPathPatterns("/api/health-infos/new")
                .addPathPatterns("/api/health-infos/{id}")
                .excludePathPatterns("/api/health-infos/{id}/details")
                .excludePathPatterns("/api/health-infos")
                .excludePathPatterns("/api/health-infos/department")

                .addPathPatterns("/api/reviews/**")
                .excludePathPatterns("/api/reviews/{id}/details")
                .excludePathPatterns("/api/reviews/hospital")
                .excludePathPatterns("/api/reviews")

                .addPathPatterns("/api/boards/**")
                .excludePathPatterns("/api/boards/{id}/details")
                .excludePathPatterns("/api/boards")

                .addPathPatterns("/api/picks/**")
                .excludePathPatterns("/api/picks/{id}/details")

                .addPathPatterns("/api/appointments/**")

                .addPathPatterns("/api/board-reply/**")
                .excludePathPatterns("/api/board-reply/board/{board-id}/**")

                .addPathPatterns("/api/review-reply/**")
                .excludePathPatterns("/api/review-reply/review/{review_id}/**")

                .addPathPatterns("/api/bus-info/**")
                .excludePathPatterns("/api/bus-info/{id}/details")
                .excludePathPatterns("/api/bus-info/area/{area}/details")

                .addPathPatterns("/payment")
                .addPathPatterns("/payment/**")

        ;

    }
}
