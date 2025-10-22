package com.example.demo.service;

import com.example.demo.dtos.request.DepositRequest;
import com.example.demo.dtos.response.DepositResponse;

public interface DepositService {
	DepositResponse deposit(DepositRequest request, String username);

	boolean validateDeposit(DepositRequest request, String username);
}
