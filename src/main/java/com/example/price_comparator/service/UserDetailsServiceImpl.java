package com.example.price_comparator.service;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.example.price_comparator.repository.UserRepository;

//import org.springframework.security.core.userdetails.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
        .map(user -> new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            // user.getRoles().stream()
            //     .map(role -> new SimpleGrantedAuthority(role.getName()))
            //     .toList()
            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
        ))
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
