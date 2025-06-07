package org.yihao.authserver.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.authserver.Enum.Role;

import java.util.List;



@Data
@NoArgsConstructor

// This prevents null fields from being included in the JSON response
//here jwtToken is null when using cookie for jwt
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private Long id;
    private String jwtToken;

    private String username;
    private List<String> roles;


    //for jwt in cookie
    public LoginResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    //for jwt in header
    public LoginResponse(Long id, String jwtToken, String username, List<String> roles) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }
}
