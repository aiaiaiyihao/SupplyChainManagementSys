package org.yihao.gatewayserver.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yihao.gatewayserver.JWT.JwtUtils;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtils jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            //需要验证的路径会进入if clause
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                //AUTHORIZATION header都没有直接报错
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("missing authorization header");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
                }

                //有AUTHORIZATION HEADER 就提取token
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
                if (authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                //验证token
/*                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
                    jwtUtil.validateJwtToken(authHeader);

                } catch (Exception e) {
                    System.out.println("invalid access...!");
//                    throw new RuntimeException("un authorized access to application");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
                }*/
                if(!jwtUtil.validateJwtToken(authHeader)){
                    log.info("invalid access...");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
                } else{
                    //authentication success then extract information and carry over the header
                    Claims claims = jwtUtil.getAllClaimsFromToken(authHeader);
                    String email = claims.get("email", String.class);
                    List<String> roles = claims.get("role", List.class);
                    Long userId = claims.get("userId", Long.class);
                    Long tableId = claims.get("tableId", Long.class);
                    String userName = claims.get("userName", String.class);
                    //usually only one role
                    String role = roles != null && !roles.isEmpty() ? roles.get(0) : "UNKNOWN";
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("email", email)
                            .header("role", role)
                            .header("id", String.valueOf(userId))
                            .header("tableId",String.valueOf(tableId))
                            .header("userName", userName)
                            .build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }

            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
