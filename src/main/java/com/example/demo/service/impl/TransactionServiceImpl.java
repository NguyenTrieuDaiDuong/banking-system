package com.example.demo.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.response.TransactionHistoryResponse;
import com.example.demo.entities.Accounts;
import com.example.demo.entities.Transactions;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCodes;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;

	private final AccountRepository accountRepository;

	@Override
	public List<TransactionHistoryResponse> getTransactionHistory(String accountNumber, String username) {
		// xác thực quyền sở hữu tài khoản
		Accounts account = validateAccountOwnership(accountNumber, username);
		List<Transactions> transactions = transactionRepository
				.findByAccountsByFromAccountIdOrAccountsByToAccountId(account, account).stream()
				.sorted(Comparator.comparing(Transactions::getCreatedAt).reversed()).collect(Collectors.toList());

		return transactions.stream().map(transaction -> mapToTransactionResponse(transaction, username))
				.collect(Collectors.toList());
	}

	@Override
	public List<TransactionHistoryResponse> getRecentTransactions(String username, int limit) {
		// TODO Auto-generated method stub

		List<Accounts> userAccounts = accountRepository.findByUsers_Username(username);
		if (userAccounts.isEmpty()) {
			throw new BusinessException(ErrorCodes.ACC_NOT_FOUND, "User has no accounts");
		}

		List<Transactions> recentTransactions = transactionRepository.findByUserUsername(username).stream()
				.sorted(Comparator.comparing(Transactions::getCreatedAt).reversed()).limit(Math.max(limit, 1))
				.collect(Collectors.toList());

		return recentTransactions.stream().map(transaction -> mapToTransactionResponse(transaction, username))
				.collect(Collectors.toList());
	}

	@Override
	public TransactionHistoryResponse getTransactionDetail(String transactionCode, String username) {
		// TODO Auto-generated method stub
		Transactions transaction = transactionRepository.findByTransactionCode(transactionCode)
				.orElseThrow(() -> new BusinessException(ErrorCodes.TXN_NOT_FOUND, "Giao dịch không tìm thấy"));
		boolean hasAccess = transaction.getAccountsByFromAccountId().getUsers().getUsername().equals(username)
				|| transaction.getAccountsByToAccountId().getUsers().getUsername().equals(username);
		if (!hasAccess) {
			throw new BusinessException(ErrorCodes.ACC_ACCESS_DENIED, "Access denied");
		}
		return mapToTransactionResponse(transaction, username);
	}

	private TransactionHistoryResponse mapToTransactionResponse(Transactions transaction, String currentUsername) {
		TransactionHistoryResponse response = new TransactionHistoryResponse();
		response.setTransactionCode(transaction.getTransactionCode());
		response.setTransactionDate(transaction.getCreatedAt());
		response.setStatus(transaction.getTransactionStatuses().getStatusName());
		response.setAmount(transaction.getAmount());
		response.setDescription(transaction.getDescription());
		response.setFromAccountNumber(transaction.getAccountsByFromAccountId().getAccountNumber());
		response.setToAccountNumber(transaction.getAccountsByToAccountId().getAccountNumber());
		response.setBeneficiaryAccount(transaction.getBeneficiaryAccount());
		response.setTransactionType(transaction.getTransactionTypes().getTypeCode());
		response.setBeneficiaryName(transaction.getBeneficiaryName());

		response.setOutgoing(isOutgoingTransaction(transaction, currentUsername));
		response.setDisplayAmount(formatDisplayAmount(transaction, response.isOutgoing()));
		response.setAccountName(getAccountName(transaction, currentUsername));
		return response;
	}

	// nếu user hiện tại là người gửi thì outgoing
	private boolean isOutgoingTransaction(Transactions transaction, String currentUsername) {
		return transaction.getAccountsByFromAccountId().getUsers().getUsername().equals(currentUsername);
	}

	private String formatDisplayAmount(Transactions transaction, boolean isOutgoing) {
		String sign = isOutgoing ? "-" : "+";
		return sign + String.format("%,.2f", transaction.getAmount());
	}

	private String getAccountName(Transactions transaction, String currentUsername) {
		if (isOutgoingTransaction(transaction, currentUsername)) {
			return transaction.getBeneficiaryName();
		} else {
			return transaction.getAccountsByFromAccountId().getUsers().getFullName();
		}
	}

	private Accounts validateAccountOwnership(String accountNumber, String username) {
		Accounts account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Account not found"));
		if (!account.getUsers().getUsername().equals(username)) {
			throw new BusinessException(ErrorCodes.ACC_ACCESS_DENIED, "Truy cập bị từ chối");
		}
		return account;
	}
}
