package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.dtos.request.UserProfileRequest;
import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.UserProfileResponse;
import com.example.demo.dtos.response.UserResponse;

public interface UserService extends UserDetailsService {
	// ADMIN METHODS + Register
	public UserResponse createUser(UserRequest request);

	public Page<UserResponse> getAllUsers(Pageable pageable);

	public UserResponse changeUserRole(Long userId, String currentUsername, String roleCode);

	public List<UserResponse> getUsersByRole(String roleCode);

	// USERS METHOD
	public UserProfileResponse getCurrentUserProfile(String username);

	public UserProfileResponse updateUserProfile(String currentUsername, UserProfileRequest request);

}
