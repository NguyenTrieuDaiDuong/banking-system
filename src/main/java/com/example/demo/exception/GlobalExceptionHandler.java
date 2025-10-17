package com.example.demo.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		;

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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
		log.error("Internal server error : {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR",
				"Đã có lỗi xảy ra, vui lòng thử lại sau nhé hihi!");
		errorResponse.setPath(request.getRequestURI());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

	}
}
