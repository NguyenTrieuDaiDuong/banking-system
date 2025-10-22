package com.example.demo.exception;

public class ErrorCodes {

	// ===== AUTHENTICATION MODULE =====
	public static final String AUTH_INVALID_CREDENTIALS = "BA_AUTH_001";
	public static final String AUTH_USER_NOT_FOUND = "BA_AUTH_002";
	public static final String AUTH_USER_EXISTS = "BA_AUTH_003";
	public static final String AUTH_ACCESS_DENIED = "BA_AUTH_004";
	public static final String AUTH_TOKEN_EXPIRED = "BA_AUTH_005";

	// ===== ACCOUNT MODULE =====
	public static final String ACC_NOT_FOUND = "BA_ACC_001";
	public static final String ACC_ACCESS_DENIED = "BA_ACC_002";
	public static final String ACC_INSUFFICIENT_BALANCE = "BA_ACC_003";
	public static final String ACC_ALREADY_EXISTS = "BA_ACC_004";
	public static final String ACC_INVALID_TYPE = "BA_ACC_005";
	public static final String ACC_UNAUTHORIZED = "BA_ACC_006";

	// ===== TRANSACTION MODULE =====
	public static final String TXN_NOT_FOUND = "BA_TXN_001";
	public static final String TXN_ACCESS_DENIED = "BA_TXN_002";
	public static final String TXN_INVALID_AMOUNT = "BA_TXN_003";
	public static final String TXN_SAME_ACCOUNT = "BA_TXN_004";

	// ===== TRANSFER MODULE =====
	public static final String TRF_VALIDATION_FAILED = "BA_TRF_001";
	public static final String TRF_INSUFFICIENT_FUNDS = "BA_TRF_002";
	public static final String TRF_LIMIT_EXCEEDED = "BA_TRF_003";

	// ===== USER MODULE =====
	public static final String USR_NOT_FOUND = "BA_USR_001";
	public static final String USR_PROFILE_UPDATE_FAILED = "BA_USR_002";
	public static final String USR_INVALID_DATA = "BA_USR_003";

	// ===== SYSTEM ERRORS =====
	public static final String SYS_DATABASE_ERROR = "BA_SYS_001";
	public static final String SYS_EXTERNAL_SERVICE_ERROR = "BA_SYS_002";
	public static final String SYS_UNEXPECTED_ERROR = "BA_SYS_999";

	// ===== VALIDATION ERRORS =====
	public static final String VALIDATION_FAILED = "BA_VAL_001";
	public static final String INVALID_REQUEST = "BA_VAL_002";
	public static final String INVALID_AMOUNT = "BA_VAL_003";

	// ===== DEPOSIT ERRORS =====
	public static final String DEPOSIT_LIMIT_EXCEEDED = "BA_DPT_001";
	public static final String INVALID_PAYMENT_METHOD = "BA_DPT_002";
	public static final String ACCOUNT_NOT_ACTIVE = "BA_DPT_003";

	// ===== BUSINESS RULE ERRORS =====
	public static final String BUSINESS_RULE_VIOLATION = "BA_BUS_001";

}
