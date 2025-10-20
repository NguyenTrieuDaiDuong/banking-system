package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.TransferRequest;
import com.example.demo.dtos.response.TransferResponse;
import com.example.demo.entities.Accounts;
import com.example.demo.entities.TransactionStatuses;
import com.example.demo.entities.TransactionTypes;
import com.example.demo.entities.Transactions;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.repositories.TransactionStatusRepository;
import com.example.demo.repositories.TransactionTypeRepository;
import com.example.demo.service.TransferService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionTypeRepository transactionTypeRepository;

	@Autowired
	private TransactionStatusRepository transactionStatusRepository;

	@Override
	@Transactional
	public TransferResponse transfer(TransferRequest request, String username) {
		System.out.println("=== START TRANSFER ===");
		System.out.println("Username: " + username);
		System.out.println("Request: " + request);
		// tự động lấy tài khoản nếu không được chỉ định
		if (request.getFromAccountNumber() == null || request.getFromAccountNumber().isEmpty()) {
			List<Accounts> userAccounts = accountRepository.findByUsers_Username(username);
			if (userAccounts.isEmpty()) {
				throw new RuntimeException("User has no acounts");
			}
			Accounts primaryAccount = findPrimaryAccount(userAccounts);
			request.setFromAccountNumber(primaryAccount.getAccountNumber());
		}
		// Validate transfer
		validateTransfer(request, username);
		// tìm tài khoản muốn chuyển và tài khoản được chuyển
		Accounts fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
				.orElseThrow(() -> new RuntimeException("From account not found"));
		Accounts toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
				.orElseThrow(() -> new RuntimeException("To account not found"));

		// kiểm tra số tiền dư
		if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
			throw new RuntimeException("insufficient balance");
		}

		TransactionTypes transferTypes = transactionTypeRepository.findByTypeCode("TRANSFER")
				.orElseThrow(() -> new RuntimeException("transfer type not found"));

		TransactionStatuses successStatus = transactionStatusRepository.findByStatusCode("COMPLETED")
				.orElseThrow(() -> new RuntimeException("Success status not found"));
		// trừ tiền tài khoản chính (tài khoản chuyển tiền)
		fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
		fromAccount.setUpdatedAt(new Date());
		accountRepository.save(fromAccount);
		// cộng tiền tài khoản được chuyển
		toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
		toAccount.setUpdatedAt(new Date());
		accountRepository.save(toAccount);
		// tạo bản lưu giao dịch
		Transactions transaction = new Transactions();
		transaction.setAccountsByFromAccountId(fromAccount);
		transaction.setAccountsByToAccountId(toAccount);
		transaction.setTransactionTypes(transferTypes);
		transaction.setTransactionStatuses(successStatus);
		transaction.setTransactionCode(generateTransactionCode());
		transaction.setAmount(request.getAmount());
		transaction.setDescription(request.getDescription());
		transaction.setBeneficiaryAccount(toAccount.getAccountNumber());
		transaction.setBeneficiaryName(toAccount.getUsers().getFullName());
		transaction.setCreatedAt(new Date());
		Transactions savedTransaction = transactionRepository.save(transaction);

		return createTransferResponse(savedTransaction, fromAccount.getBalance());

	}

	@Override
	public boolean validateTransfer(TransferRequest request, String username) {
		// kiểm tra tài khoản thuốc về user
		Accounts fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
				.orElseThrow(() -> new RuntimeException("From account not found"));
		if (!fromAccount.getUsers().getUsername().equals(username)) {
			throw new RuntimeException("You don't own the source account");
		}
		// kiểm tra tiền ( tiền > 0)
		if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RuntimeException("Amount must be greater than 0");
		}
		// kiểm tra không chuyển cho chính mình
		if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
			throw new RuntimeException("cannot transfer to the same account");

		}
		// kiểm tra tài khoản chuyển đến có tồn tại
		if (!accountRepository.findByAccountNumber(request.getToAccountNumber()).isPresent()) {
			throw new RuntimeException("Destination account not found");
		}

		return true;
	}

	// tạo code chuyển tiền
	private String generateTransactionCode() {
		return "TRX" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	}

	private Accounts findPrimaryAccount(List<Accounts> userAccounts) {
		for (Accounts account : userAccounts) {
			if ("CURRENT".equals(account.getAccountTypes().getTypeCode())) {
				return account;
			}
		}
		return userAccounts.get(0);
	}

	private TransferResponse createTransferResponse(Transactions transaction, BigDecimal newBalance) {
		TransferResponse response = new TransferResponse();
		response.setTransactionId(transaction.getTransactionCode());
		response.setFromAccountNumber(transaction.getAccountsByFromAccountId().getAccountNumber());
		response.setToAccountNumber(transaction.getAccountsByToAccountId().getAccountNumber());
		response.setAmount(transaction.getAmount());
		response.setDescription(transaction.getDescription());
		response.setStatus(transaction.getTransactionStatuses().getStatusName());
		response.setTransactionDate(transaction.getCreatedAt());
		response.setReferenceNumber(transaction.getTransactionCode());
		response.setNewBalance(newBalance);
		response.setBeneficiaryName(transaction.getBeneficiaryName());

		return response;

	}

}
