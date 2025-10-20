package com.example.demo.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.TransferRequest;
import com.example.demo.dtos.response.TransferResponse;
import com.example.demo.service.TransferService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

	@Autowired
	private TransferService transferService;

	@PostMapping("/internal")
	public ResponseEntity<?> internalTransfer(@RequestBody TransferRequest request, Authentication authentication) {
		try {
			String username = authentication.getName();
			TransferResponse response = transferService.transfer(request, username);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validateTransfer(@RequestBody TransferRequest request, Authentication authentication) {
		try {
			String username = authentication.getName();
			boolean isValid = transferService.validateTransfer(request, username);
			return ResponseEntity.ok().body("Transfer validation successful");
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
