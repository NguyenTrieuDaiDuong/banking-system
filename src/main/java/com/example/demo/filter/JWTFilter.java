package com.example.demo.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final JWTService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String bearerToken = request.getHeader("Authorization");

			if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
				String token = bearerToken.substring(7); // bỏ chuỗi "Bearer "

				if (jwtService.validToken(token)) {
					String username = jwtService.getUsernameFromJWT(token);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							username, null, getAuthorities(token));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		filterChain.doFilter(request, response);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String token) {
		// Trích xuất roles từ token (nếu có)
		List<String> roles = jwtService.extractRoles(token);
		return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
}
