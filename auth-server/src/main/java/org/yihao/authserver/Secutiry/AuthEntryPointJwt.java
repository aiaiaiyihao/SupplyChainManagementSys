package org.yihao.authserver.Secutiry;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
//AuthenticationEntryPoint is Used by ExceptionTranslationFilter to commence an authentication scheme.
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    //creates a logger for debugging and logging authentication failures.
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /*It defines a method commence() that is called
    whenever an unauthenticated request tries to access a protected resource.
    This method is triggered when an unauthenticated user tries to access a secured resource.
    It rejects the request and sends an HTTP 401 Unauthorized response.*/
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());

        //Ensures the response is in JSON format.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //Sets HTTP status 401 (Unauthorized).
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //A map is created to represent the error response in JSON format
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        //contains the reason for failure
        body.put("message", authException.getMessage());
        //captures the requested URL path
        body.put("path", request.getServletPath());

        //ObjectMapper (Jackson library) converts the Map into a JSON response.
        final ObjectMapper mapper = new ObjectMapper();
        //Writes the JSON to the response.getOutputStream().
        mapper.writeValue(response.getOutputStream(), body);
    }

}
/*When is AuthEntryPointJwt Used?
1 Spring Security Exception Handling
This entry point is triggered when an unauthenticated user tries to access a protected API.
2 JWT Authentication
If JWT authentication fails (e.g., missing or invalid token), this entry point returns a 401 Unauthorized response.*/
