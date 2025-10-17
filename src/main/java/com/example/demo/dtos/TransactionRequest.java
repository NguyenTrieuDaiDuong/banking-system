package com.example.demo.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionRequest {
	@NotNull
	private Long fromAccountId;
	private Long toAccountId;
	@NotNull
	@DecimalMin(value = "1000.0", message = "số tiền tối thiểu là 1,000 VND")
	private BigDecimal amount;
	private String description;
	private String beneficiaryName;
	private String beneficiaryAccount;
}
