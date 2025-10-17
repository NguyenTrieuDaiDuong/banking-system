package com.example.demo.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.UserRequest;
import com.example.demo.dtos.UserResponse;
import com.example.demo.service.impl.UserServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserServiceImpl userServiceImpl;

	@PostMapping("")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {

		UserResponse response = userServiceImpl.createUser(userRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> users = userServiceImpl.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@PutMapping("/{userId}/role")
	public ResponseEntity<UserResponse> changeUserRole(@PathVariable("userId") Long userId,
			@RequestParam String roleCode) {
		UserResponse response = userServiceImpl.changeUserRole(userId, roleCode);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/by-role/{roleCode}")
	public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable("roleCode") String roleCode) {
		List<UserResponse> users = userServiceImpl.getUsersByRole(roleCode);
		return ResponseEntity.ok(users);
	}
}
