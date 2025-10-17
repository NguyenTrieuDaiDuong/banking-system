package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
	private String transactionCode;
	private String typeName;
	private String statusName;
	private BigDecimal amount;
	private String fromAccountNumber;
	private String toAccountNumber;
	private String beneficiaryName;
	private LocalDateTime createdAt;

}
