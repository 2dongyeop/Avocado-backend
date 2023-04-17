package io.wisoft.capstonedesign.global.interceptor;

import io.wisoft.capstonedesign.global.exception.token.NotExistTokenException;
import io.wisoft.capstonedesign.global.exception.token.NotValidTokenException;
import io.wisoft.capstonedesign.global.jwt.AuthorizationExtractor;
import io.wisoft.capstonedesign.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(BearerAuthInterceptor.class);

    public BearerAuthInterceptor(
            final AuthorizationExtractor authExtractor,
            final JwtTokenProvider jwtTokenProvider) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception {

        logger.info(">>> interceptor.preHandle 호출");

        final String token = authExtractor.extract(request, "Bearer");

        if (!StringUtils.hasText(token)) {
            logger.error("토큰이 존재하지 않음");
            throw new NotExistTokenException("토큰이 존재하지 않음");
        }
        if (!jwtTokenProvider.validateToken(token)) {
            logger.error("유효하지 않은 토큰");
            throw new NotValidTokenException("유효하지 않은 토큰");
        }

        //토큰을 디코딩
        final String name = jwtTokenProvider.getSubject(token);

        //디코딩한 값으로 세팅
        request.setAttribute("name", name);
        return true;
    }
}