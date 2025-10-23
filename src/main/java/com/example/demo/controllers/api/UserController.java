package com.example.demo.controllers.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {

		UserResponse response = userService.createUser(userRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) { // ✅ PAGINATION
		Page<UserResponse> page = userService.getAllUsers(pageable);
		return ResponseEntity.ok(page);
	}

	@PutMapping("/{userId}/role")
	public ResponseEntity<UserResponse> changeUserRole(@PathVariable("userId") Long userId,
			@NotBlank(message = "Role code required") @RequestParam String roleCode) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = auth.getName();
		UserResponse response = userService.changeUserRole(userId, currentUsername, roleCode);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/by-role")
	public ResponseEntity<List<UserResponse>> getUsersByRole(@NotBlank @RequestParam("roleCode") String roleCode) {
		List<UserResponse> users = userService.getUsersByRole(roleCode);
		return ResponseEntity.ok(users);
	}
}
