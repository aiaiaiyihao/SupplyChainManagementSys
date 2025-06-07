package org.yihao.authserver.Secutiry.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.yihao.authserver.Mode.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/*When working with Spring Security, the User entity represents your database model,
but Spring Security requires an adapter class (UserDetails) to integrate with its authentication framework.
we can customize it*/
@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    @Getter
    private String email;
    @JsonIgnore
    private String password;

    @Getter
    private Long tableId;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password, Long tableId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.tableId = tableId;
        this.authorities = authorities;
    }


    //接收 User model 建立UserDetail对象
    public static UserDetailsImpl build(User user){
        /*.map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
        map() is used to transform each role object into a GrantedAuthority.
        role.getRoleName() presumably retrieves an enum or String representing the role name (e.g., ROLE_ADMIN, ROLE_USER).
        .name() is used if role.getRoleName() returns an enum, extracting the String representation of the enum.
        new SimpleGrantedAuthority(...)
        SimpleGrantedAuthority is a class in Spring Security that represents an authority (role or permission) granted to a user.
        It takes a role name as a string, e.g., "ROLE_ADMIN", and creates a GrantedAuthority object.*/
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role->new SimpleGrantedAuthority(role.getRoleName().name()))
//                .collect(Collectors.toList());
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(user.getRole()!=null){
            authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        }

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.getTableId(),
//                authorities
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        //check if it's null or different class
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
