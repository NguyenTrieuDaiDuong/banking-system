package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.response.AccountResponse;
import com.example.demo.entities.AccountStatuses;
import com.example.demo.entities.AccountTypes;
import com.example.demo.entities.Accounts;
import com.example.demo.entities.Users;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.AccountStatusRepository;
import com.example.demo.repositories.AccountTypeRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.UserService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountTypeRepository accountTypeRepository;

	@Autowired
	private AccountStatusRepository accountStatusRepository;

	@Autowired
	private UserService userService;

	@Override
	public AccountResponse createSavingsAccount(String username) {
		Users user = userService.findByUsername(username);
		AccountTypes savingsType = accountTypeRepository.findByTypeCode("SAVINGS")
				.orElseThrow(() -> new RuntimeException("Savings account type not found"));
		AccountStatuses activeStatus = accountStatusRepository.findByStatusCode("ACTIVE")
				.orElseThrow(() -> new RuntimeException("Active status not found"));

		String accountNumber = generateAccountNumber();

		Accounts account = new Accounts();
		account.setAccountNumber(accountNumber);
		account.setAccountStatuses(activeStatus);
		account.setAccountTypes(savingsType);
		account.setUsers(user);
		account.setBalance(BigDecimal.ZERO);
		account.setCurrency("VND");
		account.setCreatedAt(new Date());
		account.setUpdatedAt(new Date());

		Accounts savedAccount = accountRepository.save(account);
		return maptoResponse(savedAccount);
	}

	@Override
	public List<AccountResponse> getUserAccount(String username) {
		Users user = userService.findByUsername(username);
		return accountRepository.findByUsers(user).stream().map(this::maptoResponse).collect(Collectors.toList());
	}

	@Override
	public AccountResponse getAccountDetails(String accountNumber, String username) {
		Users user = userService.findByUsername(username);
		Accounts account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found"));

		if (!account.getUsers().getId().equals(user.getId())) {
			throw new RuntimeException("Access Denied");
		}
		return maptoResponse(account);
	}

	private AccountResponse maptoResponse(Accounts account) {
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
		String accountNumber;
		do {
			long randomNum = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000;
			accountNumber = String.valueOf(randomNum);
		} while (accountRepository.existsByAccountNumber(accountNumber));
		return accountNumber;
	}
}
