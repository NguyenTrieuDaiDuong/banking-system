package com.example.demo.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ChangePasswordResponse {
	private String message;
	private Long timesTamp;
}
