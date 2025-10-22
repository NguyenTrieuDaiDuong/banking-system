package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransferResponse {
	private String transactionId;
	private String fromAccountNumber;
	private String toAccountNumber;
	private BigDecimal amount;
	private String description;
	private String status;
	private LocalDateTime transactionDate;
	private String referenceNumber;
	private BigDecimal newBalance;
	private String beneficiaryName;
}
