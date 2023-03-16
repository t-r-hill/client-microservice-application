package com.example.clientmicroserviceapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

    private long id;
    private String username;

    @JsonIgnore
    private String password;

    @Builder.Default
    private List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Builder.Default
    private boolean enabled = true;

    public CustomUserDetails(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        accountNonExpired = true;
        accountNonLocked = true;
        credentialsNonExpired = true;
        enabled = true;
    }
}
