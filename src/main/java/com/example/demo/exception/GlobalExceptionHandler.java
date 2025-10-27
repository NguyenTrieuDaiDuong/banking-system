package com.example.demo.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// Xử lý BusinessException custom exception
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
		log.warn("Business exception: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(
				ex.getErrorCode() != null ? ex.getErrorCode() : "BUSINESS_ERROR", ex.getMessage());
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// Xử lý validation errors (@Valid)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());

		log.warn("Validation errors : {}", errors);

		ErrorResponse errorResponse = new ErrorResponse("VALIDATION_ERROR", "Dữ liệu không hợp lệ", errors);
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// === THÊM CÁC EXCEPTION HANDLER CHO ACCOUNT SERVICE ===

	// Xử lý AccountNotFoundException
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex,
			HttpServletRequest request) {
		log.warn("Account not found: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("ACCOUNT_NOT_FOUND", ex.getMessage());
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	// Xử lý AccessDeniedException (Spring Security)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
			HttpServletRequest request) {
		log.warn("Access denied: {} - Path: {}", ex.getMessage(), request.getRequestURI());

		ErrorResponse errorResponse = new ErrorResponse("ACCESS_DENIED", "Bạn không có quyền truy cập tài nguyên này");
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}

	// Xử lý InsufficientBalanceException
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException ex,
			HttpServletRequest request) {
		log.warn("Insufficient balance: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("INSUFFICIENT_BALANCE", ex.getMessage());
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// Xử lý AccountAlreadyLockedException
	@ExceptionHandler(AccountAlreadyLockedException.class)
	public ResponseEntity<ErrorResponse> handleAccountAlreadyLockedException(AccountAlreadyLockedException ex,
			HttpServletRequest request) {
		log.warn("Account already locked: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("ACCOUNT_ALREADY_LOCKED", ex.getMessage());
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// Xử lý AccountNotLockedException (khi unlock account không khóa)
	@ExceptionHandler(AccountNotLockedException.class)
	public ResponseEntity<ErrorResponse> handleAccountNotLockedException(AccountNotLockedException ex,
			HttpServletRequest request) {
		log.warn("Account not locked: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("ACCOUNT_NOT_LOCKED", ex.getMessage());
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// Xử lý IllegalStateException (cho các trạng thái không hợp lệ)
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex,
			HttpServletRequest request) {
		log.warn("Illegal state: {} - Path: {}", ex.getMessage(), request.getRequestURI());

		ErrorResponse errorResponse = new ErrorResponse("INVALID_OPERATION", ex.getMessage());
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// Xử lý IllegalArgumentException (cho tham số không hợp lệ)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
			HttpServletRequest request) {
		log.warn("Illegal argument: {} - Path: {}", ex.getMessage(), request.getRequestURI());

		ErrorResponse errorResponse = new ErrorResponse("INVALID_PARAMETER", ex.getMessage());
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// Xử lý UserNotFoundException
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex,
			HttpServletRequest request) {
		log.warn("User not found: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("USER_NOT_FOUND", ex.getMessage());
		errorResponse.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
		log.error("Internal server error : {} - Path: {}", ex.getMessage(), request.getRequestURI(), ex);

		ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR",
				"Đã có lỗi xảy ra, vui lòng thử lại sau nhé hihi!");
		errorResponse.setPath(request.getRequestURI());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}