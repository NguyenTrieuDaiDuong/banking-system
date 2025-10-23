package com.example.demo.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.response.AccountResponse;
import com.example.demo.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
public class AccountController {
	@Autowired
	private AccountService accountService;

	@PostMapping("/createSavings")
	public ResponseEntity<AccountResponse> createSavingsAccount(Authentication authentication) {
		String username = authentication.getName();
		AccountResponse account = accountService.createSavingsAccount(username);
		return ResponseEntity.ok(account);
	}

	@GetMapping("/my-accounts")
	public ResponseEntity<List<AccountResponse>> getMyAccounts(Authentication authentication) {
		String username = authentication.getName();
		List<AccountResponse> myAccount = accountService.getUserAccounts(username);
		return ResponseEntity.ok(myAccount);
	}

	@GetMapping("/{accountNumber}")
	public ResponseEntity<AccountResponse> getAccountDetails(@PathVariable String accountNumber,
			Authentication authentication) {
		String username = authentication.getName();
		AccountResponse response = accountService.getAccountDetails(accountNumber, username);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{accountNumber}/lock")
	public ResponseEntity<AccountResponse> lockAccount(@PathVariable String accountNumber,
			Authentication authentication) {
		String username = authentication.getName();
		AccountResponse response = accountService.lockAccount(accountNumber, username);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{accountNumber}/unlock")
	public ResponseEntity<AccountResponse> unlockAccount(@PathVariable String accountNumber,
			Authentication authentication) {
		String username = authentication.getName();
		AccountResponse response = accountService.unlockAccount(accountNumber, username);
		return ResponseEntity.ok(response);
	}
}
