package com.example.demo.controllers.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.response.TransactionHistoryResponse;
import com.example.demo.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping("/history/{accountNumber}")
	public ResponseEntity<?> getTransactionHistory(@PathVariable String accountNumber, Authentication authentication) {
		try {
			String username = authentication.getName();
			List<TransactionHistoryResponse> transactions = transactionService.getTransactionHistory(accountNumber,
					username);
			return ResponseEntity.ok(transactions);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/recent")
	public ResponseEntity<?> getTransactionDetail(@RequestParam(defaultValue = "10") int limit,
			Authentication authentication) {
		try {
			String username = authentication.getName();
			List<TransactionHistoryResponse> transactions = transactionService.getRecentTransactions(username, limit);
			return ResponseEntity.ok(transactions);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{transactionCode}")
	public ResponseEntity<?> getTransactionDetail(@PathVariable("transactionCode") String transactionCode,
			Authentication authentication) {
		try {
			String username = authentication.getName();
			TransactionHistoryResponse transactions = transactionService.getTransactionDetail(transactionCode,
					username);
			return ResponseEntity.ok(transactions);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
