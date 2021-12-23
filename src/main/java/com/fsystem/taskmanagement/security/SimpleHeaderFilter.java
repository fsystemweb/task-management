package com.fsystem.taskmanagement.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.fsystem.taskmanagement.constants.Contants.FSYSTEM_HEADER_KEY;
import static com.fsystem.taskmanagement.constants.Contants.FSYSTEM_HEADER_VALUE;

@Component
public class SimpleHeaderFilter extends OncePerRequestFilter {

    private static final List<String> AUTH_WHITELIST = Arrays.asList(
            "/swagger-resources",
            "/swagger-ui/",
            "/v3/api-docs");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // OPTIONS should always work
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String val = request.getHeader(FSYSTEM_HEADER_KEY);
        if (!checkSwaggerUrl(request) && (val == null || !val.equals(FSYSTEM_HEADER_VALUE))) {
            response.setStatus(401);
            response.getWriter().append("Not authorized");
            return;
        }

        filterChain.doFilter(request, response);
    }


    private boolean checkSwaggerUrl(HttpServletRequest request){
        String url = request.getRequestURL().toString();

        return AUTH_WHITELIST.stream().anyMatch(url::contains);
    }
}
