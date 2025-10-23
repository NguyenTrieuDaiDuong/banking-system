package com.example.demo.dtos.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WithdrawalRequest {
	private String accountNumber;
	private BigDecimal amount;
	private String description;
	private String withdrawalMethod;
}
