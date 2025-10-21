package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.UserResponse;

public interface UserService extends UserDetailsService {

	public UserResponse createUser(UserRequest request);

	public Page<UserResponse> getAllUsers(Pageable pageable);

	public UserResponse changeUserRole(Long userId, String currentUsername, String roleCode);

	public List<UserResponse> getUsersByRole(String roleCode);
}
