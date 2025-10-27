package com.example.demo.exception;

public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException(String message) {
		super(message);
	}

	public AccountNotFoundException(String accountNumber, String username) {
		super(String.format("Tài khoản %s không tồn tại hoặc không thuộc về quyền sở hữu của user %s", accountNumber,
				username));
	}
}
