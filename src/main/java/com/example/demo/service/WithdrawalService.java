package com.example.demo.service;

import com.example.demo.dtos.request.WithdrawalRequest;
import com.example.demo.dtos.response.WithdrawalResponse;

public interface WithdrawalService {
	WithdrawalResponse withdrawal(WithdrawalRequest request, String username);

	boolean validateWithdrawal(WithdrawalRequest request, String username);
}
