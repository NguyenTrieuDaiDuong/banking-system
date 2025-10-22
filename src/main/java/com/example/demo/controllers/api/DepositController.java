package com.example.demo.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.DepositRequest;
import com.example.demo.dtos.response.DepositResponse;
import com.example.demo.service.DepositService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class DepositController {

	private final DepositService depositService;

	@GetMapping("/test")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("Deposit API is working!");
	}

	@PostMapping
	public ResponseEntity<?> deposit(@RequestBody DepositRequest request, Authentication authentication) {
		try {
			String username = authentication.getName();
			DepositResponse response = depositService.deposit(request, username);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validateDeposit(@RequestBody DepositRequest request, Authentication authentication) {
		try {
			String username = authentication.getName();
			boolean isValid = depositService.validateDeposit(request, username);
			if (isValid) {
				return ResponseEntity.ok("Deposit validation successful");
			} else {
				return ResponseEntity.badRequest().body("Deposit validation failed");
			}
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
