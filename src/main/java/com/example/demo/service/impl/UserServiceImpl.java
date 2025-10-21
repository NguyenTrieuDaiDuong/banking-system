package com.example.demo.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.UserRequest;
import com.example.demo.dtos.response.UserResponse;
import com.example.demo.entities.UserRoles;
import com.example.demo.entities.Users;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ErrorCodes;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserRoleRepository;
import com.example.demo.repositories.UserStatusRepository;
import com.example.demo.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRoleRepository userRoleRepository;
	private final UserStatusRepository userStatusRepository;

	@Transactional
	@Override
	public UserResponse createUser(UserRequest request) {

		validateUserRequest(request);

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new BusinessException(ErrorCodes.AUTH_USER_EXISTS, "USERNAME_EXISTS");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BusinessException(ErrorCodes.USR_INVALID_DATA, "EMAIL_EXISTS");
		}
		if (request.getIdentityCard() != null && userRepository.existsByIdentityCard(request.getIdentityCard())) {
			throw new BusinessException(ErrorCodes.USR_INVALID_DATA, "IDENTITY_CARD_EXIST");
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
				.orElseThrow(() -> new BusinessException(ErrorCodes.AUTH_ACCESS_DENIED, "ROLE_NOT_FOUND"));
		user.setUserRoles(userRole);

		user.setUserStatuses(userStatusRepository.findByStatusCode("ACTIVE")
				.orElseThrow(() -> new BusinessException(ErrorCodes.USR_INVALID_DATA, "Status ACTIVE không tồn tại")));
		Users saveUser = userRepository.save(user);

		return mapToUserResponse(saveUser);

	}

	@Transactional
	public UserResponse changeUserRole(Long userId, String currentUsername, String roleCode) {
		Users currentUser = userRepository.findByUsername(currentUsername)
				.orElseThrow(() -> new BusinessException(ErrorCodes.USR_NOT_FOUND, ""));
		if (!"ADMIN".equalsIgnoreCase(currentUser.getUserRoles().getRoleCode())) {
			throw new BusinessException(ErrorCodes.AUTH_ACCESS_DENIED, "Only ADMIN can change role");
		}
		if (userId.equals(currentUser.getId())) {
			throw new BusinessException(ErrorCodes.AUTH_ACCESS_DENIED, "Cannot change one's own role");
		}
		Users user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(ErrorCodes.USR_NOT_FOUND, "USER_NOT_FOUND"));
		UserRoles newRoles = userRoleRepository.findByRoleCode(roleCode)
				.orElseThrow(() -> new BusinessException(ErrorCodes.AUTH_ACCESS_DENIED, "ROLE_NOT_FOUND"));

		user.setUserRoles(newRoles);

		Users updatedUser = userRepository.save(user);
		return mapToUserResponse(updatedUser);
	}

	@Override
	public List<UserResponse> getUsersByRole(String roleCode) {
		if (roleCode == null || roleCode.trim().isEmpty()) {
			throw new BusinessException(ErrorCodes.USR_INVALID_DATA, "Role code required");
		}
		List<Users> users = userRepository.getByUserRoles_RoleCode(roleCode);
		return users.stream().map(this::mapToUserResponse).toList();
	}

	@Override
	public Page<UserResponse> getAllUsers(Pageable pageable) {
		Page<Users> userPage = userRepository.findAll(pageable);
		return userPage.map(this::mapToUserResponse);
	}

	private UserResponse mapToUserResponse(Users user) {
		UserResponse response = new UserResponse();

		response.setId(user.getId());
		response.setEmail(user.getEmail());
		response.setUsername(user.getUsername());
		response.setFullName(user.getFullName());
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found :" + username));

		List<GrantedAuthority> authorities = List
				.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRoles().getRoleCode()));

		return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
	}

	private void validateUserRequest(UserRequest request) {
		if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
			throw new BusinessException(ErrorCodes.USR_INVALID_DATA, "Username required");
		}
		if (request.getEmail() == null || !request.getEmail().contains("@")) {
			throw new BusinessException(ErrorCodes.USR_INVALID_DATA, "Invalid Email");
		}
		if (request.getPassword() == null || request.getPassword().length() < 6) {
			throw new BusinessException(ErrorCodes.USR_INVALID_DATA, "Password min 6 chars");
		}
	}
}
