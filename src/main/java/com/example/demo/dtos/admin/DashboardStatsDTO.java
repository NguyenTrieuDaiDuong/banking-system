package com.example.demo.dtos.admin;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDTO {
	private Long totalUsers;
	private Long totalAccounts;
	private Long totalTransactions;
	private BigDecimal totalBalance;
	private BigDecimal totalRevenue;
	private Long activeUsersTody;
	private Long todayTransactions;
}
