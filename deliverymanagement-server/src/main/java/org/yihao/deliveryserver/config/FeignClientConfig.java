package org.yihao.deliveryserver.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Configuration
public class FeignClientConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Get current HTTP request
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();

                // Copy Authorization header
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null) {
                    requestTemplate.header("Authorization", authHeader);
                }

                // Copy custom headers like x-user-id, x-user-role, x-user-email
                String userId = request.getHeader("id");
                String userRole = request.getHeader("role");
                String userEmail = request.getHeader("email");

                if (userId != null) requestTemplate.header("id", userId);
                if (userRole != null) requestTemplate.header("role", userRole);
                if (userEmail != null) requestTemplate.header("email", userEmail);
            }
        };
    }
}
