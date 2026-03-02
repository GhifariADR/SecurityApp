package com.explore.securityApp.config;

import com.explore.securityApp.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        try {
            processToken(request);
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(
                    "{ \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Token is not valid or expired\" }"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void processToken(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            logger.info("No Bearer header found");
            return;
        }

        String jwtToken = header.substring(7);

        if (jwtUtil.isTokenExpired(jwtToken)) {
            logger.info("Token Expired");
            throw new RuntimeException("Token expired");
        }

        String username = jwtUtil.extractUsername(jwtToken);

        if (username == null) {
            throw new RuntimeException("Invalid token");
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(auth);

        logger.info("Authenticated user: " + username);
    }
}


