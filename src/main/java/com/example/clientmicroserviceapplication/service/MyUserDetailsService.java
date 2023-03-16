package com.example.clientmicroserviceapplication.service;

import com.example.clientmicroserviceapplication.model.CustomUserDetails;
import com.example.clientmicroserviceapplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.getUser(username);
        if (user != null) {
            return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword());
        } else {
            return null;
        }
    }
}
