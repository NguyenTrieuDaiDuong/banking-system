package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.WithdrawalRequest;
import com.example.demo.dtos.response.WithdrawalResponse;
import com.example.demo.entities.Accounts;
import com.example.demo.entities.TransactionStatuses;
import com.example.demo.entities.TransactionTypes;
import com.example.demo.entities.Transactions;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCodes;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.repositories.TransactionStatusRepository;
import com.example.demo.repositories.TransactionTypeRepository;
import com.example.demo.service.WithdrawalService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {
	private final AccountRepository accountRepository;
	private final TransactionStatusRepository transactionStatusRepository;
	private final TransactionTypeRepository transactionTypeRepository;
	private final TransactionRepository transactionRepository;

	@Override
	@Transactional
	public WithdrawalResponse withdrawal(WithdrawalRequest request, String username) {
		validateWithdrawal(request, username);

		Accounts fromAccount = accountRepository.findByAccountNumber(request.getAccountNumber())
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Account not found"));

		Accounts systemAccount = accountRepository.findByAccountNumber("SYSTEM_WITHDRAWAL").orElseThrow(
				() -> new BusinessException(ErrorCodes.SYS_DATABASE_ERROR, "System withdrawal account not found"));

		TransactionTypes withdrawalType = transactionTypeRepository.findByTypeCode("WITHDRAWAL")
				.orElseThrow(() -> new BusinessException(ErrorCodes.SYS_UNEXPECTED_ERROR, "Withdrawal type not found"));

		TransactionStatuses withdrawalStatus = transactionStatusRepository.findByStatusCode("COMPLETED").orElseThrow(
				() -> new BusinessException(ErrorCodes.SYS_UNEXPECTED_ERROR, "Completed status not found"));

		BigDecimal newBalance = fromAccount.getBalance().subtract(request.getAmount());
		fromAccount.setBalance(newBalance);
		fromAccount.setUpdatedAt(LocalDateTime.now());
		accountRepository.save(fromAccount);

		Transactions transaction = createTransactionsRecord(fromAccount, systemAccount, withdrawalType,
				withdrawalStatus, request);
		Transactions savedTransaction = transactionRepository.save(transaction);

		return mapToWithdrawalResponse(savedTransaction, newBalance, request.getWithdrawalMethod());
	}

	@Override
	public boolean validateWithdrawal(WithdrawalRequest request, String username) {
		Accounts account = accountRepository.findByAccountNumber(request.getAccountNumber())
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Account not found"));

		if (!account.getUsers().getUsername().equals(username)) {
			throw new BusinessException(ErrorCodes.ACC_ACCESS_DENIED, "Access denied to this account");
		}

		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException(ErrorCodes.ACC_INSUFFICIENT_BALANCE, "Amount must be greater than 0");
		}
		if (account.getBalance().compareTo(request.getAmount()) < 0) {
			throw new BusinessException(ErrorCodes.ACC_INSUFFICIENT_BALANCE, "Insufficient balance");
		}
		BigDecimal minWithdrawal = new BigDecimal("50000");
		if (request.getAmount().compareTo(minWithdrawal) < 0) {
			throw new BusinessException(ErrorCodes.TXN_ACCESS_DENIED, "Minimum withdrawal amount is 50,000 VND");
		}
		if (!"ACTIVE".equals(account.getAccountStatuses().getStatusCode())) {
			throw new BusinessException(ErrorCodes.ACC_ACCESS_DENIED, "Account is not active");
		}
		if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
			throw new BusinessException(ErrorCodes.VALIDATION_FAILED, "Description cannot be empty");
		}
		if (request.getWithdrawalMethod() == null || !isValidWithdrawalMethod(request.getWithdrawalMethod())) {
			throw new BusinessException(ErrorCodes.INVALID_WITHDRAWAL_METHOD, "Withdrawal method is required");
		}
		return true;
	}

	private String generateTransactionCode() {
		return "WDL" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}

	private String getWithdrawalMethodDisplay(String withdrawalMethod) {
		switch (withdrawalMethod) {
		case "CASH":
			return "tiền mặt";
		case "BANK_TRANSFER":
			return "chuyển khoản ngân hàng";
		case "E_WALLET":
			return " ví điện tử ";
		case "CREDIT_CARD":
			return "thẻ tín dụng";
		default:
			return "";
		}
	}

	private boolean isValidWithdrawalMethod(String withdrawalMethod) {
		return Arrays.asList("BANK_TRANSFER", "CASH", "CREDIT_CARD", "E_WALLET").contains(withdrawalMethod);
	}

	private String buildWithdrawalDescription(WithdrawalRequest request) {
		String method = getWithdrawalMethodDisplay(request.getWithdrawalMethod());
		return String.format("rút tiền %s - %s", method, request.getDescription());
	}

	private Transactions createTransactionsRecord(Accounts fromAccount, Accounts toAccount,
			TransactionTypes transactionTypes, TransactionStatuses transactionStatuses, WithdrawalRequest request) {
		Transactions transaction = new Transactions();
		transaction.setAccountsByFromAccountId(fromAccount);
		transaction.setAccountsByToAccountId(toAccount);
		transaction.setDescription(buildWithdrawalDescription(request));
		transaction.setTransactionStatuses(transactionStatuses);
		transaction.setTransactionTypes(transactionTypes);
		transaction.setTransactionCode(generateTransactionCode());
		transaction.setAmount(request.getAmount());
		transaction.setBeneficiaryName(toAccount.getUsers().getFullName());
		transaction.setBeneficiaryAccount(toAccount.getAccountNumber());
		transaction.setCreatedAt(LocalDateTime.now());

		return transaction;
	}

	private WithdrawalResponse mapToWithdrawalResponse(Transactions transaction, BigDecimal newBalance,
			String withdrawalMethod) {
		WithdrawalResponse response = new WithdrawalResponse();
		response.setTransactionCode(transaction.getTransactionCode());
		response.setAccountNumber(transaction.getAccountsByFromAccountId().getAccountNumber());
		response.setAmount(transaction.getAmount());
		response.setNewBalance(newBalance);
		response.setDescription(transaction.getDescription());
		response.setWithdrawalMethod(withdrawalMethod);
		response.setStatus(transaction.getTransactionStatuses().getStatusName());
		response.setTransactionDate(transaction.getCreatedAt());
		return response;
	}
}
