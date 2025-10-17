package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.UserRoles;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoles, Long> {
	Optional<UserRoles> findByRoleCode(String roleCode);

	boolean existsByRoleCode(String roleCode);
}
