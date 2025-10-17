package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.entities.Users;

public interface UserService extends UserDetailsService {
	Users findByUsername(String username);
}
