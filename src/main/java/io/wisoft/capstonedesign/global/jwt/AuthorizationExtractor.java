package io.wisoft.capstonedesign.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.util.Strings;


import java.util.Enumeration;

@Component
public class AuthorizationExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    public String extract(
            final HttpServletRequest request,
            final String type) {

        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);

        while (headers.hasMoreElements()) {
            String value = headers.nextElement();

            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }

        return Strings.EMPTY;
    }
}
