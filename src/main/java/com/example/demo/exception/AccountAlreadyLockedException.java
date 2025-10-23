package com.example.demo.exception;

public class AccountAlreadyLockedException extends RuntimeException {

	public AccountAlreadyLockedException(String message) {
		super(message);
	}

	public static AccountAlreadyLockedException forAccount(String accountNumber) {
		return new AccountAlreadyLockedException(String.format("Tài khoản %s đã bị khóa trước đó", accountNumber));
	}
}
