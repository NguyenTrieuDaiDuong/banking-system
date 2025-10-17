package com.example.demo.service;

import java.util.List;

public interface JWTService {

	public String generateToken(String username);

	public String getUsernameFromJWT(String token);

	public boolean validToken(String token);

	public List<String> extractRoles(String token);
}
