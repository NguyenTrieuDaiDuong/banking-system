package com.example.demo.service;

import java.util.List;

import com.example.demo.dtos.response.TransactionHistoryResponse;

public interface TransactionService {

	public List<TransactionHistoryResponse> getTransactionHistory(String accountNumber, String username);

	public List<TransactionHistoryResponse> getRecentTransactions(String username, int limit);

	public TransactionHistoryResponse getTransactionDetail(String transactionCode, String username);

}
