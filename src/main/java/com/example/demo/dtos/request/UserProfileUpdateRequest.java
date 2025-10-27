package com.example.demo.dtos.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private LocalDate dateOfBirth;
}
