package com.example.demo.dtos.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class AccountResponse {
	private Long id;
	private String accountNumber;
	private String accountType;
	private Integer accountTypeId;
	private BigDecimal balance;
	private String currency;
	private String status;
	private Integer statusId;
	private Date createdAt;
	private Date updatedAt;
	private String userFullName;
	private Long userId;
}
