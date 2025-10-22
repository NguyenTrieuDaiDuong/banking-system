package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.DepositRequest;
import com.example.demo.dtos.response.DepositResponse;
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
import com.example.demo.service.DepositService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final TransactionTypeRepository transactionTypeRepository;
	private final TransactionStatusRepository transactionStatusRepository;

	@Override
	@Transactional
	public DepositResponse deposit(DepositRequest request, String username) {
		validateDeposit(request, username);
		Accounts toAccount = accountRepository.findByAccountNumber(request.getAccountNumber())
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Account not found"));
		Accounts systemAccount = accountRepository.findByAccountNumber("SYSTEM_DEPOSIT")
				.orElseThrow(() -> new BusinessException(ErrorCodes.SYS_UNEXPECTED_ERROR, "System Error"));

		TransactionTypes depositType = transactionTypeRepository.findByTypeCode("DEPOSIT").orElseThrow(
				() -> new BusinessException(ErrorCodes.SYS_UNEXPECTED_ERROR, "Transaction type does not exists"));

		TransactionStatuses depositStatus = transactionStatusRepository.findByStatusCode("COMPLETED").orElseThrow(
				() -> new BusinessException(ErrorCodes.SYS_UNEXPECTED_ERROR, "Transaction status does not exists"));

		BigDecimal newBalance = toAccount.getBalance().add(request.getAmount());

		toAccount.setBalance(newBalance);

		toAccount.setUpdatedAt(LocalDateTime.now());

		accountRepository.save(toAccount);

		Transactions transaction = createTransactionRecord(systemAccount, toAccount, depositType, depositStatus,
				request);

		Transactions savedTransaction = transactionRepository.save(transaction);

		return mapToDepositResponse(savedTransaction, newBalance, request.getPaymentMethod());
	}

	@Override
	public boolean validateDeposit(DepositRequest request, String username) {
		// TODO Auto-generated method stub
		Accounts account = accountRepository.findByAccountNumber(request.getAccountNumber())
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Account not found"));
		BigDecimal maxDeposit = new BigDecimal("100000000");
		if (request.getAmount().compareTo(maxDeposit) > 0) {
			throw new BusinessException(ErrorCodes.TRF_LIMIT_EXCEEDED, "Maximum deposit amount is 100,000,000 VND");
		}
		if (!account.getUsers().getUsername().equals(username)) {
			throw new BusinessException(ErrorCodes.ACC_ACCESS_DENIED, "Access denied to this account");
		}
		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException(ErrorCodes.TXN_INVALID_AMOUNT, "Amount must be greater than 0");
		}
		if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
			throw new BusinessException(ErrorCodes.VALIDATION_FAILED, "Transaction description cannot be blank");
		}
		if (!"ACTIVE".equals(account.getAccountStatuses().getStatusCode())) {
			throw new BusinessException(ErrorCodes.ACC_ACCESS_DENIED, "Account is not in active status");
		}
		if (request.getPaymentMethod() == null || !isValidPaymentMethod(request.getPaymentMethod())) {
			throw new BusinessException(ErrorCodes.INVALID_PAYMENT_METHOD, "Payment method is required");
		}
		return true;
	}

	private String generateTransactionCode() {
		return "DPT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	}

	private boolean isValidPaymentMethod(String paymentMethod) {
		return Arrays.asList("BANK_TRANSFER", "CASH", "CREDIT_CARD", "E_WALLET").contains(paymentMethod);
	}

	private Transactions createTransactionRecord(Accounts fromAccount, Accounts toAccount,
			TransactionTypes transactionTypes, TransactionStatuses transactionStatus, DepositRequest request) {
		Transactions transaction = new Transactions();
		transaction.setAccountsByFromAccountId(fromAccount);
		transaction.setAccountsByToAccountId(toAccount);
		transaction.setTransactionTypes(transactionTypes);
		transaction.setTransactionStatuses(transactionStatus);
		transaction.setTransactionCode(generateTransactionCode());
		transaction.setAmount(request.getAmount());
		transaction.setCreatedAt(LocalDateTime.now());
		transaction.setDescription(buildDepositDescription(request));
		transaction.setBeneficiaryName(toAccount.getUsers().getFullName());
		transaction.setBeneficiaryAccount(toAccount.getAccountNumber());
		return transaction;
	}

	private String getPaymentMethodDisplay(String paymentMethod) {
		switch (paymentMethod) {
		case "BANK_TRANSFER":
			return "chuyển khoản ngân hàng";
		case "CASH":
			return "tiền mặt";
		case "CREDIT_CARD":
			return "thẻ tín dụng";
		case "E_WALLET":
			return "ví điện tử ";
		default:
			return "";
		}
	}

	private String buildDepositDescription(DepositRequest request) {
		String method = getPaymentMethodDisplay(request.getPaymentMethod());
		return String.format("Nạp tiền %s - %s", method, request.getDescription());
	}

	private DepositResponse mapToDepositResponse(Transactions transaction, BigDecimal newBalance,
			String paymentMethod) {
		DepositResponse response = new DepositResponse();
		response.setTransactionCode(transaction.getTransactionCode());
		response.setAccountNumber(transaction.getAccountsByToAccountId().getAccountNumber());
		response.setAmount(transaction.getAmount());
		response.setNewBalance(newBalance);
		response.setDescription(transaction.getDescription());
		response.setPaymentMethod(paymentMethod);
		response.setStatus(transaction.getTransactionStatuses().getStatusName());
		response.setTransactionDate(transaction.getCreatedAt());
		return response;
	}
}
