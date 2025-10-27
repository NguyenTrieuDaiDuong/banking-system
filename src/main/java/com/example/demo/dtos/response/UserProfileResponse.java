package com.example.demo.dtos.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserProfileResponse {
	private Long id;
	private String username;
	private String email;
	private String fullName;
	private String phone;
	private String address;
	private LocalDateTime createdAt;
}
