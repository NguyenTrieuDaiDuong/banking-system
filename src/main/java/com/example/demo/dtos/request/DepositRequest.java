package com.example.demo.dtos.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DepositRequest {
	private String accountNumber;
	private BigDecimal amount;
	private String description;
	private String paymentMethod;
}
