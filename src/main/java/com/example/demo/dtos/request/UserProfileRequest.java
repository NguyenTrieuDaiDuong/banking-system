package com.example.demo.dtos.request;

import lombok.Data;

@Data
public class UserProfileRequest {
	private String fullName;
	private String email;
	private String phone;
	private String address;
}
