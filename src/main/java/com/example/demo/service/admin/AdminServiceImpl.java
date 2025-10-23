package com.example.demo.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.admin.DashboardStatsDTO;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	public final UserRepository userRepository;
	public final AccountRepository accountRepository;
	public final TransactionRepository transactionRepository;

	@Transactional(readOnly = true)
	public DashboardStatsDTO getDashboardStats() {
		return DashboardStatsDTO.builder().totalUsers(userRepository.count()).totalAccounts(accountRepository.count())
				.totalTransactions(transactionRepository.count()).totalBalance(accountRepository.getTotalBalance())
				.totalRevenue(transactionRepository.getTotalRevenue())
				.activeUsersTody(userRepository.countActiveUsers())
				.todayTransactions(transactionRepository.getTodayTransactionCount()).build();
	}
}
