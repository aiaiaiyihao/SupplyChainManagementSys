package org.yihao.gatewayserver.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/*This RouteValidator class determines which API endpoints should be secured (require authentication)
and which should be open (accessible without authentication) in your Spring Cloud Gateway.*/
@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/signin",
            "/auth/signup/driver",
            "/auth/signup/supplier",
            "/auth/admin/signup",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
