package com.example.demo.dtos.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransferRequest {
	private String fromAccountNumber;
	private String toAccountNumber;
	private BigDecimal amount;
	private String description;
}
