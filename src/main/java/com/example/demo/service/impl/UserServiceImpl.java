package com.example.demo.service.impl;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.UserRequest;
import com.example.demo.dtos.UserResponse;
import com.example.demo.entities.UserRoles;
import com.example.demo.entities.Users;
import com.example.demo.exception.BusinessException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserRoleRepository;
import com.example.demo.repositories.UserStatusRepository;
import com.example.demo.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRoleRepository userRoleRepository;
	private final UserStatusRepository userStatusRepository;

	public UserResponse createUser(UserRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new BusinessException("username đã tồn tại", "USERNAME_EXISTS");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BusinessException("email đã tồn tại", "EMAIL_EXISTS");
		}
		if (request.getIdentityCard() != null && userRepository.existsByIdentityCard(request.getIdentityCard())) {
			throw new BusinessException("CMMD/CCCD đã tồn tại", "IDENTITY_CARD_EXIST");
		}

		Users user = new Users();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFullName(request.getFullName());
		user.setPhone(request.getPhone());
		user.setDateOfBirth(request.getDateOfBirth());
		user.setIdentityCard(request.getIdentityCard());
		user.setAddress(request.getAddress());

		UserRoles userRole = userRoleRepository.findByRoleCode("USER")
				.orElseThrow(() -> new BusinessException("Role User không tồn tại", "ROLE_NOT_FOUND"));

		user.setUserRoles(userRole);

		user.setUserStatuses(userStatusRepository.findByStatusCode("ACTIVE")
				.orElseThrow(() -> new BusinessException("Status ACTIVE không tồn tại")));

		Users saveUser = userRepository.save(user);

		return mapToUserResponse(saveUser);

	}

	public UserResponse changeUserRole(Long userId, String roleCode) {
		Users user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException("User không tồn tại", "USER_NOT_FOUND"));

		UserRoles newRoles = userRoleRepository.findByRoleCode(roleCode)
				.orElseThrow(() -> new BusinessException("Role không tồn tại", "ROLE_NOT_FOUND"));

		user.setUserRoles(newRoles);
		Users updatedUser = userRepository.save(user);

		return mapToUserResponse(updatedUser);

	}

	public List<UserResponse> getUsersByRole(String roleCode) {
		List<Users> users = userRepository.getByUserRoles_RoleCode(roleCode);
		return users.stream().map(this::mapToUserResponse).toList();
	}

	public List<UserResponse> getAllUsers() {
		List<Users> users = userRepository.findAllWithRelations();
		return users.stream().map(this::mapToUserResponse).toList();
	}

	private UserResponse mapToUserResponse(Users user) {
		UserResponse response = new UserResponse();

		response.setId(user.getId());
		response.setEmail(user.getEmail());
		response.setUsername(user.getUsername());
		;
		response.setFullName(user.getFullName());
		;
		response.setPhone(user.getPhone());

		if (user.getUserRoles() != null) {
			response.setRoleCode(user.getUserRoles().getRoleCode());
			response.setRoleName(user.getUserRoles().getRoleName());
		}
		if (user.getUserStatuses() != null) {
			response.setStatusName(user.getUserStatuses().getStatusName());
		}
		response.setCreateAt(user.getCreatedAt());

		return response;
	}

	@Override
	public Users findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Username not found: " + username);
		}
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getUserRoles().getRoleName()));

		System.out.println("Found user: " + user.getUsername()); // Debug
		System.out.println("Password in DB: " + user.getPassword()); // Debug
		return new User(user.getUsername(), user.getPassword(), authorities);
	}

}
