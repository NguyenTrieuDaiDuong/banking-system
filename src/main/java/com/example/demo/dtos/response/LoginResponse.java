package com.example.demo.dtos.response;

public class LoginResponse {

	private String token;
	private String username;
	private String type = "Bearer ";

	public LoginResponse(String token) {
		this.token = token;
	}

	public LoginResponse(String token, String username) {
		this.token = token;
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
