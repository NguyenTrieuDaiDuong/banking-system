package com.example.demo.service;

import java.util.List;

import com.example.demo.dtos.response.AccountResponse;

public interface AccountService {
	AccountResponse createSavingsAccount(String username);

	List<AccountResponse> getUserAccounts(String username);

	AccountResponse getAccountDetails(String accountNumber, String username);
}
