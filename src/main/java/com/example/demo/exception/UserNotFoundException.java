package com.example.demo.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}

	public static UserNotFoundException forUsername(String username) {
		return new UserNotFoundException(String.format("Người dùng %s không tồn tại", username));
	}
}
