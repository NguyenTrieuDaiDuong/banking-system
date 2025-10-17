package com.example.demo.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.LoginResponse;
import com.example.demo.service.JWTService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JWTService jwtService;
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String token = jwtService.generateToken(userDetails.getUsername());
			return ResponseEntity.ok(new LoginResponse(token));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
		}
	}
}
