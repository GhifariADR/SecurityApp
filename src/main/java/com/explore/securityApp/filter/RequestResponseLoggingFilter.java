package com.explore.securityApp.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper(request);

        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        long duration = System.currentTimeMillis() - startTime;

        String requestBody = new String(
                requestWrapper.getContentAsByteArray(),
                request.getCharacterEncoding()
        );

        String responseBody = new String(
                responseWrapper.getContentAsByteArray(),
                response.getCharacterEncoding()
        );

        if (!isSensitiveEndpoint(request.getRequestURI())){
            log.info("\nMethod: {}\nURI: {}\nStatus: {}\nDuration: {} ms\nRequest: {}\nResponse: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    responseWrapper.getStatus(),
                    duration,
                    requestBody,
                    responseBody
            );
        } else {
            log.info("\nMethod: {}\nURI: {}\nStatus: {}\nDuration: {} ms\nResponse: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    responseWrapper.getStatus(),
                    duration,
                    responseBody
            );
        }



        responseWrapper.copyBodyToResponse();
    }

    private boolean isSensitiveEndpoint(String uri){
        return uri.contains("/auth/login")|| uri.contains("/auth/register")|| uri.contains("/auth/refresh");
    }

}
