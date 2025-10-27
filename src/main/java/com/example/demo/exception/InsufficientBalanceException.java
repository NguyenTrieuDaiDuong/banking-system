package com.example.demo.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
	public InsufficientBalanceException(String message) {
		super(message);
	}

	public InsufficientBalanceException(String accountNumber, BigDecimal currentBalance, BigDecimal requiredAmount) {
		super(String.format("Tài khoản %s không đủ số dư. Số dư hiện tại: %s, Số tiền cần: %s", accountNumber,
				currentBalance, requiredAmount));
	}
}
