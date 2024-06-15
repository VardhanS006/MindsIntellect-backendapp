package com.backend.backendapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.backendapp.model.User;
import com.backend.backendapp.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Load User from db
        User user = (username.contains("@")?
                    userRepository.findByEmail(username):
                    userRepository.findByUsername(username))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }
    
}
