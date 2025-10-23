package com.example.demo.exception;

public class AccountNotLockedException extends RuntimeException {
	public AccountNotLockedException(String message) {
		super(message);
	}

	public static AccountNotLockedException forAccount(String accountNumber) {
		return new AccountNotLockedException(String.format("Tài khoản %s không ở trạng thái khóa", accountNumber));
	}
}
