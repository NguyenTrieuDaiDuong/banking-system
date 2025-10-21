package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TransactionHistoryResponse {
	private String transactionCode;
	private Date transactionDate;
	private String status;
	private BigDecimal amount;
	private String description;
	private String fromAccountNumber;
	private String toAccountNumber;
	private String beneficiaryName;
	private String beneficiaryAccount;
	private String transactionType;

	private boolean isOutgoing;
	private String displayAmount;
	private String accountName;
}
