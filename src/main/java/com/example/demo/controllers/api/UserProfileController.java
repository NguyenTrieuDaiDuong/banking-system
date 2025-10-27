package com.example.demo.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.UserProfileRequest;
import com.example.demo.dtos.response.UserProfileResponse;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserProfileController {
	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
		String username = authentication.getName(); // Lấy từ JWT
		UserProfileResponse profile = userService.getCurrentUserProfile(username);
		return ResponseEntity.ok(profile);
	}

	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(Authentication authentication,
			@Valid @RequestBody UserProfileRequest request) {
		try {
			String username = authentication.getName();
			UserProfileResponse updated = userService.updateUserProfile(username, request);
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
