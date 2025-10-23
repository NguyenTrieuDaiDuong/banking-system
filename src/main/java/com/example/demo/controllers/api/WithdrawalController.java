package com.example.demo.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.WithdrawalRequest;
import com.example.demo.dtos.response.WithdrawalResponse;
import com.example.demo.service.WithdrawalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/withdrawal")
@RequiredArgsConstructor
public class WithdrawalController {
	public final WithdrawalService withdrawalService;

	@PostMapping
	public ResponseEntity<?> withdrawal(@RequestBody WithdrawalRequest request, Authentication authentication) {
		try {
			String username = authentication.getName();
			WithdrawalResponse response = withdrawalService.withdrawal(request, username);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validateWithdrawal(@RequestBody WithdrawalRequest request, Authentication authentication) {
		try {
			String username = authentication.getName();
			boolean isValid = withdrawalService.validateWithdrawal(request, username);
			return ResponseEntity.ok(isValid);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
