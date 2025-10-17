package com.example.demo.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.demo.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JWTService {

	@Autowired
	private Environment environment;

	@Override
	public String generateToken(String username) {
		String secret = environment.getProperty("app.jwt.secret");
		long expiry = Long.parseLong(environment.getProperty("app.jwt.expiration"));
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiry);
		return Jwts.builder().subject(username).issuedAt(now).expiration(expiryDate)
				.signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256).compact();
	}

	@Override
	public String getUsernameFromJWT(String token) {
		String secret = environment.getProperty("app.jwt.secret");
		Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token)
				.getPayload();
		return claims.getSubject();
	}

	@Override
	public boolean validToken(String token) {
		try {
			String secret = environment.getProperty("app.jwt.secret");
			Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> extractRoles(String token) {
		Claims claims = extractAllClaims(token);
		return claims.get("roles", List.class);
	}

	private Claims extractAllClaims(String token) {
		String secret = environment.getProperty("app.jwt.secret");
		return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token)
				.getPayload();
	}

	// Thêm phương thức để generate token với roles (nếu cần)
	public String generateTokenWithRoles(String username, List<String> roles) {
		String secret = environment.getProperty("app.jwt.secret");
		long expiry = Long.parseLong(environment.getProperty("app.jwt.expiration"));
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiry);

		return Jwts.builder().subject(username).claim("roles", roles) // Thêm roles vào token
				.issuedAt(now).expiration(expiryDate).signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
				.compact();
	}
}