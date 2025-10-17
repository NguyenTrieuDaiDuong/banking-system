package com.example.demo.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.response.AccountResponse;
import com.example.demo.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	@Autowired
	private AccountService accountService;

	@PostMapping("/createSavings")
	public ResponseEntity<AccountResponse> createSavingsAccount(Authentication authentication) {
		String username = authentication.getName();
		AccountResponse account = accountService.createSavingsAccount(username);
		return ResponseEntity.ok(account);
	}
}
