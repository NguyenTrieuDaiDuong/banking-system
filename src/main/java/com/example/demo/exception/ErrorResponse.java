package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	private LocalDateTime timestamp;
	private String errorCode;
	private String message;
	private List<String> details;
	private String path;

	public ErrorResponse(String errorCode, String message) {
		this.timestamp = LocalDateTime.now();
		this.errorCode = errorCode;
		this.message = message;
	}

	public ErrorResponse(String errorCode, String message, List<String> details) {
		this.timestamp = LocalDateTime.now();
		this.errorCode = errorCode;
		this.message = message;
		this.details = details;
	}

	public ErrorResponse(String errorCode, String message, String path) {
		this.timestamp = LocalDateTime.now();
		this.errorCode = errorCode;
		this.message = message;
		this.path = path;
	}

	public ErrorResponse(String errorCode, String message, List<String> details, String path) {
		this.timestamp = LocalDateTime.now();
		this.errorCode = errorCode;
		this.message = message;
		this.details = details;
		this.path = path;
	}
}
