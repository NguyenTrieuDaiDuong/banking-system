package com.example.demo.filter;

import java.io.IOException;
import java.util.Arrays;
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
					List<String> roles = jwtService.extractRoles(token);
					Collection<? extends GrantedAuthority> authorities = getAuthorities(roles);

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							username, null, authorities);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		filterChain.doFilter(request, response);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
		// Trích xuất roles từ token (nếu có)
		if (roles == null || roles.isEmpty()) {
			return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
}
