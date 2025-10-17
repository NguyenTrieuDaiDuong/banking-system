package com.example.demo.dtos.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserResponse {
	private Long id;
	private String username;
	private String email;
	private String fullName;
	private String phone;
	private String roleName;
	private String roleCode;
	private String statusName;
	private LocalDateTime createAt;

}
