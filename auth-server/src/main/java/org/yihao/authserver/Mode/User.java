package org.yihao.authserver.Mode;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.yihao.authserver.Enum.Role;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(max = 20)
    private String userName;

    @NotBlank
    @Size(max=50)
    @Email
    private String email;

    @NotBlank
    @Size(max=120)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Long tableId;

    public User(String userName,String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    public User(String userName,String email, String password,Role role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
