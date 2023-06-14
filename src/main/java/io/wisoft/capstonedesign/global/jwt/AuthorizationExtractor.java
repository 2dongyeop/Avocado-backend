package io.wisoft.capstonedesign.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.util.Strings;


import java.util.Enumeration;

@Component
public class AuthorizationExtractor {
    private final String AUTHORIZATION = "Authorization";

    public String extract(
            final HttpServletRequest request,
            final String type) {

        final Enumeration<String> headers = request.getHeaders(AUTHORIZATION);

        while (headers.hasMoreElements()) {
            final String value = headers.nextElement();

            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }

        return Strings.EMPTY;
    }
}
