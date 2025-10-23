package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.response.AccountResponse;
import com.example.demo.entities.AccountStatuses;
import com.example.demo.entities.AccountTypes;
import com.example.demo.entities.Accounts;
import com.example.demo.entities.Users;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCodes;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.AccountStatusRepository;
import com.example.demo.repositories.AccountTypeRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.AccountService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	private final AccountTypeRepository accountTypeRepository;

	private final AccountStatusRepository accountStatusRepository;

	private final UserRepository userRepository;

	private void validateUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			throw new BusinessException(ErrorCodes.USR_INVALID_DATA, "Username required");
		}
	}

	@Transactional
	@Override
	public AccountResponse createSavingsAccount(String username) {
		validateUsername(username);

		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessException(ErrorCodes.USR_NOT_FOUND, "User not found"));
		AccountTypes savingsType = accountTypeRepository.findByTypeCode("SAVINGS")
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Savings account type not found"));
		AccountStatuses activeStatus = accountStatusRepository.findByStatusCode("ACTIVE")
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Active status not found"));

		String accountNumber = generateAccountNumber();

		Accounts account = new Accounts();
		account.setAccountNumber(accountNumber);
		account.setAccountStatuses(activeStatus);
		account.setAccountTypes(savingsType);
		account.setUsers(user);
		account.setBalance(BigDecimal.ZERO);
		account.setCurrency("VND");
		account.setCreatedAt(LocalDateTime.now());
		account.setUpdatedAt(LocalDateTime.now());

		Accounts savedAccount = accountRepository.save(account);
		return mapToResponse(savedAccount);
	}

	@Override
	public List<AccountResponse> getUserAccounts(String username) {
		validateUsername(username);
		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessException(ErrorCodes.USR_NOT_FOUND, "User not found"));
		return accountRepository.findByUsers(user).stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public AccountResponse getAccountDetails(String accountNumber, String username) {
		validateUsername(username);
		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessException(ErrorCodes.USR_NOT_FOUND, "User not found"));
		Accounts account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Account not found"));

		if (!account.getUsers().getId().equals(user.getId())) {
			throw new BusinessException(ErrorCodes.ACC_ACCESS_DENIED, "Access Denied");
		}
		return mapToResponse(account);
	}

	private AccountResponse mapToResponse(Accounts account) {
		AccountResponse response = new AccountResponse();
		response.setId(account.getId());
		response.setAccountNumber(account.getAccountNumber());
		if (account.getAccountTypes() != null) {
			response.setAccountType(account.getAccountTypes().getTypeName());
			response.setAccountTypeId(account.getAccountTypes().getId());
		}
		if (account.getAccountStatuses() != null) {
			response.setStatus(account.getAccountStatuses().getStatusName());
			response.setStatusId(account.getAccountStatuses().getId());
		}
		response.setBalance(account.getBalance());
		response.setCurrency(account.getCurrency());
		response.setCreatedAt(account.getCreatedAt());
		response.setUpdatedAt(account.getUpdatedAt());
		if (account.getUsers() != null) {
			response.setUserFullName(account.getUsers().getFullName());
			response.setUserId(account.getUsers().getId());
		}

		return response;
	}

	private String generateAccountNumber() {
		int maxRetries = 10;
		for (int i = 0; i < maxRetries; i++) {
			String accountNumber = String.format("ACC%010d", (long) (Math.random() * 900_000_000L));
			if (!accountRepository.existsByAccountNumber(accountNumber)) {
				return accountNumber;
			}
		}
		throw new BusinessException(ErrorCodes.USR_INVALID_DATA, "Cannot generate unique account number ");
	}

	@Override
	public AccountResponse unlockAccount(String accountNumber, String username) {
		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessException(ErrorCodes.USR_NOT_FOUND, "User not found"));
		Accounts account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Account not found"));

		if (!account.getUsers().getId().equals(user.getId())) {
			throw new BusinessException(ErrorCodes.AUTH_ACCESS_DENIED, "Cannot unlock another user's account");
		}

		AccountStatuses activeStatus = accountStatusRepository.findByStatusCode("ACTIVE")
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Actice status not found"));

		account.setAccountStatuses(activeStatus);
		account.setUpdatedAt(LocalDateTime.now());

		accountRepository.save(account);
		return mapToResponse(account);
	}

	@Override
	public AccountResponse lockAccount(String accountNumber, String username) {
		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessException(ErrorCodes.USR_NOT_FOUND, "User not found"));

		Accounts account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Account not found"));

		if (!account.getUsers().getId().equals(user.getId())) {
			throw new BusinessException(ErrorCodes.ACC_ACCESS_DENIED, "Cannot lock another user's account");
		}

		AccountStatuses inactiveStatus = accountStatusRepository.findByStatusCode("INACTIVE")
				.orElseThrow(() -> new BusinessException(ErrorCodes.ACC_NOT_FOUND, "Inactive status not found"));
		account.setAccountStatuses(inactiveStatus);
		account.setUpdatedAt(LocalDateTime.now());

		accountRepository.save(account);

		return mapToResponse(account);
	}
}
