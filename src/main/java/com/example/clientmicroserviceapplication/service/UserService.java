package com.example.clientmicroserviceapplication.service;

import com.example.clientmicroserviceapplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @Autowired
    PasswordEncoder encoder;

    public User getUser(String username) throws UsernameNotFoundException {
        ResponseEntity<User> userResponse = restTemplate.getForEntity("http://USER-MICROSERVICE/user/username/" + username, User.class);
        if (userResponse.getStatusCode().is2xxSuccessful() && userResponse.getBody() != null){
            return userResponse.getBody();
        }
        return null;
    }

    public User createNewUser(User user) throws IllegalStateException{
        checkPassword(user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));
        ResponseEntity<User> newUserResponse = restTemplate.postForEntity("http://USER-MICROSERVICE/user", user, User.class);
        if (newUserResponse.getStatusCode().equals(HttpStatus.CREATED) && newUserResponse.getBody() != null){
            return newUserResponse.getBody();
        } else{
            throw new IllegalStateException("Registration failed");
        }
    }

    private void checkPassword(String password) throws IllegalStateException {
        if (password == null) {
            throw new IllegalStateException("You must set a password");
        }
        if (password.length() < 6) {
            throw new IllegalStateException("Password is too short. Must be longer than 6 characters");
        }
    }
}
