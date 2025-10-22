package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DepositResponse {
	private String transactionCode;
	private String accountNumber;
	private BigDecimal amount;
	private BigDecimal newBalance;
	private String status;
	private String description;
	private String paymentMethod;
	private LocalDateTime transactionDate;
}
