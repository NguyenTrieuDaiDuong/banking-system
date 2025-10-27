package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WithdrawalResponse {
	private String transactionCode;
	private String accountNumber;
	private BigDecimal amount;
	private BigDecimal newBalance;
	private String description;
	private String withdrawalMethod;
	private String status;
	private LocalDateTime transactionDate;
}
